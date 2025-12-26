import { Component, OnInit } from '@angular/core';
import { AuthService } from '../auth.service';
import { CartService } from '../cart.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {
  cartItemCount = 0;
  isLoggedIn = false;
  searchQuery = '';

  constructor(public authService: AuthService, private cartService: CartService, private router: Router) { }

  ngOnInit(): void {
    this.isLoggedIn = this.authService.isLoggedIn();

    // Subscribe to cart changes
    this.cartService.cart$.subscribe(cart => {
      if (cart) {
        this.cartItemCount = cart.cartItems ? cart.cartItems.length : 0;
      } else {
        this.cartItemCount = 0;
      }
    });

    // Refresh cart if logged in (in case we reload page)
    if (this.isLoggedIn) {
      this.cartService.getCart().subscribe();
    }
  }

  logout() {
    this.authService.logout();
    this.isLoggedIn = false;
    this.router.navigate(['/login']).then(() => {
      window.location.reload(); // Reload to clear all states
    });
  }

  onSearch() {
    this.router.navigate(['/products'], { queryParams: { search: this.searchQuery } });
  }
}

