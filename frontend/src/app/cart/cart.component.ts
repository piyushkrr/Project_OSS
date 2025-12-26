import { Component, OnInit } from '@angular/core';
import { CartService } from '../cart.service';
import { Router } from '@angular/router';
import { ToastService } from '../toast.service';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})
export class CartComponent implements OnInit {
  cart: any = null;
  loading: boolean = true;
  errorMessage: string = '';


  constructor(
    private cartService: CartService, 
    private router: Router,
    private toastService: ToastService
  ) { }



  ngOnInit(): void {
    this.cartService.getCart().subscribe({
      next: (cart) => {
        this.cart = cart;
        this.loading = false;
      },
      error: (err) => {
        this.errorMessage = 'Failed to load your cart. Please try again.';
        this.loading = false;
        console.error(err);
      }
    });
  }


  updateQuantity(item: any, quantity: number) {
    if (quantity < 1) return;
    this.cartService.addToCart(item.product.id, quantity - item.quantity).subscribe(cart => {
        this.cart = cart;
        this.toastService.show('Quantity updated', 'success');
    });
  }

  removeItem(productId: number) {
    this.cartService.removeFromCart(productId).subscribe(cart => {
      this.cart = cart;
      this.toastService.show('Item removed from cart', 'info');
    });
  }

  clearCart() {
    this.cartService.clearCart().subscribe(() => {
      this.cart = null;
      this.toastService.show('Cart cleared', 'info');
    });
  }

  checkout() {
    this.router.navigate(['/checkout']);
  }
}

