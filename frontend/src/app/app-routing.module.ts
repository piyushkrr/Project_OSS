import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ProductListComponent } from './product-list/product-list.component';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { CartComponent } from './cart/cart.component';

import { CheckoutComponent } from './checkout/checkout.component';
import { OrderHistoryComponent } from './order-history/order-history.component';
import { AdminProductListComponent } from './admin/admin-product-list/admin-product-list.component';
import { ProductFormComponent } from './admin/product-form/product-form.component';
import { AdminOrderListComponent } from './admin/admin-order-list/admin-order-list.component';
import { ProfileComponent } from './profile/profile.component';
import { ProductDetailComponent } from './product-detail/product-detail.component';
import { AuthGuard } from './auth.guard';



import { HomeComponent } from './home/home.component';
import { AdminCouponsComponent } from './admin/admin-coupons/admin-coupons.component';

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'products', component: ProductListComponent },
  { path: 'products/:id', component: ProductDetailComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'cart', component: CartComponent },
  { path: 'checkout', component: CheckoutComponent, canActivate: [AuthGuard] },
  { path: 'orders', component: OrderHistoryComponent, canActivate: [AuthGuard] },
  { path: 'profile', component: ProfileComponent, canActivate: [AuthGuard] },

  { path: 'admin/products', component: AdminProductListComponent, canActivate: [AuthGuard] },
  { path: 'admin/products/new', component: ProductFormComponent, canActivate: [AuthGuard] },
  { path: 'admin/products/edit/:id', component: ProductFormComponent, canActivate: [AuthGuard] },
  { path: 'admin/orders', component: AdminOrderListComponent, canActivate: [AuthGuard] },
  { path: 'admin/coupons', component: AdminCouponsComponent, canActivate: [AuthGuard] }
];



@NgModule({
  imports: [RouterModule.forRoot(routes, {
    scrollPositionRestoration: 'top', // Scroll to top on navigation
    anchorScrolling: 'enabled'
  })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
