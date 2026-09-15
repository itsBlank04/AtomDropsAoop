import { Link } from 'react-router-dom'

export default function ComingSoonPage({ title, description }: { title: string; description?: string }) {
  return (
    <div className="flex min-h-[60vh] flex-col items-center justify-center px-6 py-20 text-center">
      <div className="flex h-20 w-20 items-center justify-center rounded-3xl bg-[#f9f5f0]">
        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round" className="h-10 w-10 text-[#8c7564]">
          <path d="M12 6v6l4 2" />
          <circle cx="12" cy="12" r="10" />
        </svg>
      </div>
      <h1 className="mt-6 font-[Fraunces] text-3xl font-semibold text-[#221b16]">{title} — Coming Soon</h1>
      <p className="mt-3 max-w-md text-sm leading-relaxed text-[#8c7564]">
        {description || 'This feature is under development and will be available soon. Stay tuned!'}
      </p>
      <Link
        to="/"
        className="mt-8 rounded-full bg-[#221b16] px-6 py-2.5 text-sm font-semibold text-[#f9f5f0] transition-colors hover:bg-[#3a3028]"
      >
        Back to Home
      </Link>
    </div>
  )
}
