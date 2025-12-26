import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { FormsModule } from '@angular/forms';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { AuthInterceptor } from './auth.interceptor';

import { NavbarComponent } from './navbar/navbar.component';

import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';

import { ProductListComponent } from './product-list/product-list.component';
import { CartComponent } from './cart/cart.component';
import { CheckoutComponent } from './checkout/checkout.component';
import { OrderHistoryComponent } from './order-history/order-history.component';
import { AdminProductListComponent } from './admin/admin-product-list/admin-product-list.component';
import { ProductFormComponent } from './admin/product-form/product-form.component';
import { AdminOrderListComponent } from './admin/admin-order-list/admin-order-list.component';
import { ProfileComponent } from './profile/profile.component';
import { ToastComponent } from './toast/toast.component';
import { ProductDetailComponent } from './product-detail/product-detail.component';
import { HomeComponent } from './home/home.component';
import { AdminCouponsComponent } from './admin/admin-coupons/admin-coupons.component';

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    LoginComponent,
    RegisterComponent,
    ProductListComponent,
    CartComponent,
    CheckoutComponent,
    OrderHistoryComponent,
    AdminProductListComponent,
    ProductFormComponent,
    AdminOrderListComponent,
    ProfileComponent,
    ToastComponent,
    ProductDetailComponent,
    HomeComponent,
    AdminCouponsComponent
  ],


  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
