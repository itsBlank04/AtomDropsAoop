import { useEffect, useLayoutEffect } from 'react'
import { Routes, Route, useNavigate, useLocation } from 'react-router-dom'
import AuthModal from './components/AuthModal'
import SiteLayout from './components/SiteLayout'
import { useAuthModal } from './contexts/AuthModalContext'
import HomePage from './pages/HomePage'
import ProductListPage from './pages/products/ProductListPage'
import ProductDetailPage from './pages/products/ProductDetailPage'
import CartPage from './pages/cart/CartPage'
import CheckoutPage from './pages/cart/CheckoutPage'
import OrderSuccessPage from './pages/cart/OrderSuccessPage'
import AccountOrdersPage from './pages/account/AccountOrdersPage'
import ProfilePage from './pages/account/ProfilePage'
import WishlistPage from './pages/account/WishlistPage'
import AddressesPage from './pages/account/AddressesPage'
import NotificationsPage from './pages/account/NotificationsPage'
import ComingSoonPage from './pages/ComingSoonPage'
import VendorProductsPage from './pages/vendor/VendorProductsPage'
import VendorDashboardPage from './pages/vendor/VendorDashboardPage'
import VendorOrdersPage from './pages/vendor/VendorOrdersPage'
import VendorShopPage from './pages/vendor/VendorShopPage'
import VendorSubscriptionsPage from './pages/vendor/VendorSubscriptionsPage'
import VendorShopManagerPage from './pages/vendor/VendorShopManagerPage'
import VendorFinancialPage from './pages/vendor/VendorFinancialPage'
import VendorReviewsPage from './pages/vendor/VendorReviewsPage'
import VendorShopSetupPage from './pages/vendor/VendorShopSetupPage'



import AdminDashboardPage from './pages/admin/AdminDashboardPage'


function ScrollToTop() {
  const { pathname } = useLocation()
  useLayoutEffect(() => {
    window.scrollTo(0, 0)
  }, [pathname])
  useEffect(() => {
    if ('scrollRestoration' in history) history.scrollRestoration = 'manual'
    return () => { if ('scrollRestoration' in history) history.scrollRestoration = 'auto' }
  }, [])
  return null
}

function AuthRedirect({ tab }: { tab: 'signin' | 'register' }) {
  const { openModal } = useAuthModal()
  const navigate = useNavigate()

  useEffect(() => {
    openModal(tab)
    navigate('/', { replace: true })
  // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [])

  return null
}

export default function App() {
  return (
    <>
      <AuthModal />
      <ScrollToTop />
      <Routes>
        <Route path="/auth/login" element={<AuthRedirect tab="signin" />} />
        <Route path="/auth/register" element={<AuthRedirect tab="register" />} />

        {/* Products */}
        <Route path="/products" element={<SiteLayout><ProductListPage /></SiteLayout>} />
        <Route path="/products/:id" element={<SiteLayout><ProductDetailPage /></SiteLayout>} />
        <Route path="/shop/:slug" element={<SiteLayout><VendorShopPage /></SiteLayout>} />

        {/* Shopping */}
        <Route path="/cart" element={<SiteLayout><CartPage /></SiteLayout>} />
        <Route path="/checkout" element={<SiteLayout><CheckoutPage /></SiteLayout>} />
        <Route path="/order-success/:id" element={<SiteLayout><OrderSuccessPage /></SiteLayout>} />

        {/* Used Items */}
        <Route path="/used" element={<SiteLayout><ComingSoonPage title="Used Items" description="Buy and sell pre-owned electronics. The used marketplace is coming soon." /></SiteLayout>} />

        {/* Auctions */}
        <Route path="/auctions" element={<SiteLayout><ComingSoonPage title="Auctions" description="Browse and bid on exclusive items. Auctions will be launching soon — stay tuned!" /></SiteLayout>} />

        {/* Repair */}
        <Route path="/repair" element={<SiteLayout><ComingSoonPage title="Repair Hub" description="Find certified technicians for your devices. The repair marketplace is coming soon." /></SiteLayout>} />

        {/* Account */}
        <Route path="/account/orders" element={<SiteLayout><AccountOrdersPage /></SiteLayout>} />
        <Route path="/profile" element={<SiteLayout><ProfilePage /></SiteLayout>} />
        <Route path="/wishlist" element={<SiteLayout><WishlistPage /></SiteLayout>} />
        <Route path="/addresses" element={<SiteLayout><AddressesPage /></SiteLayout>} />
        <Route path="/notifications" element={<SiteLayout><NotificationsPage /></SiteLayout>} />
        <Route path="/messages" element={<SiteLayout><ComingSoonPage title="Messages" description="Real-time messaging with sellers and buyers is coming soon." /></SiteLayout>} />

        {/* Vendor */}
        <Route path="/vendor/dashboard" element={<SiteLayout><VendorDashboardPage /></SiteLayout>} />
        <Route path="/vendor/products" element={<SiteLayout><VendorProductsPage /></SiteLayout>} />
        <Route path="/vendor/orders" element={<SiteLayout><VendorOrdersPage /></SiteLayout>} />
        <Route path="/vendor/auctions" element={<SiteLayout><ComingSoonPage title="Vendor Auctions" description="Create and manage auction listings for your shop. This feature is coming soon." /></SiteLayout>} />
        <Route path="/vendor/subscription" element={<SiteLayout><VendorSubscriptionsPage /></SiteLayout>} />
        <Route path="/vendor/shops" element={<SiteLayout><VendorShopManagerPage /></SiteLayout>} />
        <Route path="/vendor/shops/setup" element={<SiteLayout><VendorShopSetupPage /></SiteLayout>} />
        <Route path="/vendor/shops/:shopId/products" element={<SiteLayout><VendorProductsPage /></SiteLayout>} />
        <Route path="/vendor/financials" element={<SiteLayout><VendorFinancialPage /></SiteLayout>} />
        <Route path="/vendor/reviews" element={<SiteLayout><VendorReviewsPage /></SiteLayout>} />

        {/* Admin */}
        <Route path="/admin" element={<SiteLayout><AdminDashboardPage /></SiteLayout>} />

        {/* Home */}
        <Route path="*" element={<SiteLayout><HomePage /></SiteLayout>} />
      </Routes>
    </>
  )
}
