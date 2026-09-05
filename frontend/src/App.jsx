import React, { useEffect, useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from './assets/vite.svg'
import heroImg from './assets/hero.png'
import './App.css'
import Products from './pages/Products'
import { Routes, Route, Navigate, useNavigate } from 'react-router-dom'
import HomePage from './pages/HomePage'
import ScrollToTop from './utils/ScrollToTop'
import Navbar from './components/shared/Navbar'
import About from './pages/About'
import Contact from './pages/Contact'
import Cart from './pages/Cart'
import { Toaster } from 'react-hot-toast'
import LogInPage from './pages/LogInPage'
import PrivateRoute from './components/shared/PrivateRoute'
import RegisterPage from './pages/RegisterPage'
import CheckoutPage from './pages/CheckoutPage'
import { useSelector, useDispatch } from 'react-redux'
import { checkAuth } from './store/actions'
import OrderPlacedPage from './pages/orderPlacedPage'
import AdminPanel from './pages/AdminPanel'
import Dashboard from './components/admin/dashboard/Dashboard'
import Sellers from './components/admin/sellers/Sellers'
import AdminOrders from './components/admin/orders/AdminOrders'
import AdminProducts from './components/admin/products/AdminProducts'
import Category from './components/admin/category/Category'
import { primeCsrfCookie, setupInterceptors } from './api/api'
import MyOrdersPage from './pages/myOrdersPage'
import ProfilePage from './pages/ProfilePage'
import SellerApplications from './components/admin/sellers/SellerApplications'
import ApplyForSellerPage from './pages/ApplyForSellerPage'
import LoadingScreen from './components/loaders/LoadingScreen'
import { Analytics } from "@vercel/analytics/react"

function App() {

const navigate = useNavigate();
  const dispatch = useDispatch();
  const {user, authChecked} = useSelector((state)=>state.auth)
  const cart = useSelector((state)=>state.cart.cart);
  const isAdmin = user?.roles?.includes("ROLE_ADMIN");


 useEffect(() => {
      setupInterceptors(navigate);
      primeCsrfCookie();
  }, [navigate]);

  useEffect(() => {
        dispatch(checkAuth());
    }, [dispatch]);

  if (!authChecked) {
    return <LoadingScreen />;
  }

  return (
      <React.Fragment>
        <Navbar />
        <Routes>
          <Route path='/' element={ <HomePage />}/>
          <Route path='/products' element={ <Products />}/>
          <Route path='/about' element={ <About />}/>
          <Route path='/contact' element={ <Contact />}/>
          <Route path='/cart' element={ <Cart />}/>



          <Route path='/' element={<PrivateRoute />}>
            <Route path='/checkout' element={ cart.length > 0 ? <CheckoutPage />: <Navigate to='/cart' replace/>}/>
             <Route path='/profile/orders' element={<MyOrdersPage />} />
             <Route path='/profile' element={<ProfilePage />} />
              <Route path='/order-confirmation' element={<OrderPlacedPage/>}/>
          </Route> 

          <Route element={<PrivateRoute userOnly/>}>
            <Route path='/apply-for-seller' element={<ApplyForSellerPage/>}/>
          </Route>

          <Route element={<PrivateRoute adminOnly />}>
              <Route path="/admin" element={<AdminPanel />}>
                  <Route index element={<Dashboard />} />
                  <Route path="orders" element={<AdminOrders />} />
                  <Route path="products" element={<AdminProducts />} />
                  <Route path="categories" element={<Category />} />
                  <Route path="sellers" element={<Sellers />} />
                  <Route path="sellers-applications" element={<SellerApplications/>} />
              </Route>
          </Route>

          <Route element={<PrivateRoute sellerOnly />}>
              <Route path="/seller" element={<AdminPanel />}>
                  <Route index element={<Navigate to="orders" replace />} />
                  <Route path="orders" element={<AdminOrders />} />
                  <Route path="products" element={<AdminProducts />} />
              </Route>
          </Route>

          <Route path='/' element={<PrivateRoute publicPage />}>
            <Route path='/login' element={ <LogInPage />}/>
            <Route path='/register' element={ <RegisterPage />}/>
          </Route>

        </Routes>
      <Toaster position='bottom-center'/>
      <Analytics />
    </React.Fragment>
  )
}

export default App
