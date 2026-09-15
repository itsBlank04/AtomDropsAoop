import { createClient, type SupabaseClient } from '@supabase/supabase-js'

const supabaseUrl = import.meta.env.VITE_SUPABASE_URL || ''
const supabaseAnonKey = import.meta.env.VITE_SUPABASE_ANON_KEY || ''

export const supabase: SupabaseClient | null = supabaseUrl && supabaseAnonKey
  ? createClient(supabaseUrl, supabaseAnonKey)
  : null

function requireClient(): SupabaseClient {
  if (!supabase) {
    throw new Error('Supabase is not configured. Set VITE_SUPABASE_URL and VITE_SUPABASE_ANON_KEY in the frontend .env file.')
  }
  return supabase
}

// Default bucket for all media
const BUCKET = 'atomdrops'

/**
 * Upload a single file to Supabase Storage.
 * @param folder - e.g. 'avatars', 'products/123', 'used-items/456'
 * @param file - the File object from an input
 * @returns the public URL of the uploaded file
 */
export async function uploadFile(folder: string, file: File): Promise<string> {
  const client = requireClient()
  const ext = file.name.split('.').pop()
  const uniqueName = `${Date.now()}-${Math.random().toString(36).slice(2, 8)}.${ext}`
  const path = `${folder}/${uniqueName}`

  const { data, error } = await client.storage
    .from(BUCKET)
    .upload(path, file, {
      cacheControl: '3600',
      upsert: false,
      contentType: file.type,
    })

  if (error) {
    console.error('Supabase upload error:', error)
    throw new Error(error.message || 'Upload failed')
  }

  const { data: urlData } = client.storage
    .from(BUCKET)
    .getPublicUrl(data.path)

  return urlData.publicUrl
}

/**
 * Upload multiple files and return their public URLs.
 */
export async function uploadFiles(
  folder: string,
  files: File[],
  onProgress?: (uploaded: number, total: number) => void
): Promise<string[]> {
  const urls: string[] = []
  for (let i = 0; i < files.length; i++) {
    const url = await uploadFile(folder, files[i])
    urls.push(url)
    onProgress?.(i + 1, files.length)
  }
  return urls
}

/**
 * Delete a file from Supabase Storage by its path.
 */
export async function deleteFile(path: string): Promise<void> {
  // Extract path from full URL
  const match = path.match(/\/storage\/v1\/object\/public\/atomdrops\/(.+)/)
  if (match) {
    await requireClient().storage.from(BUCKET).remove([match[1]])
  }
}

/**
 * Check if a file is an image or video.
 */
export function getFileType(file: File): 'image' | 'video' | 'other' {
  if (file.type.startsWith('image/')) return 'image'
  if (file.type.startsWith('video/')) return 'video'
  return 'other'
}

/**
 * Validate file before upload.
 */
export function validateFile(
  file: File,
  maxSizeMB: number = 10,
  allowedTypes: string[] = ['image/jpeg', 'image/png', 'image/webp', 'image/gif', 'video/mp4', 'video/webm']
): string | null {
  if (!allowedTypes.includes(file.type)) {
    return `File type ${file.type} is not allowed. Allowed: ${allowedTypes.map(t => t.split('/')[1]).join(', ')}`
  }
  if (file.size > maxSizeMB * 1024 * 1024) {
    return `File size ${(file.size / 1024 / 1024).toFixed(1)}MB exceeds ${maxSizeMB}MB limit`
  }
  return null
}
