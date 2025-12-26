import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ProductService } from '../product.service';
import { CartService } from '../cart.service';
import { AuthService } from '../auth.service';
import { ToastService } from '../toast.service';

@Component({
  selector: 'app-product-detail',
  templateUrl: './product-detail.component.html',
  styleUrls: ['./product-detail.component.css']
})
export class ProductDetailComponent implements OnInit {
  product: any = null;
  loading: boolean = true;
  errorMessage: string = '';
  quantity: number = 1;
  
  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private productService: ProductService,
    private cartService: CartService,
    public authService: AuthService,
    private toastService: ToastService
  ) { }

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.productService.getProductById(parseInt(id)).subscribe({
        next: (data) => {
          this.product = data;
          this.loading = false;
        },
        error: (err) => {
          this.errorMessage = 'Product not found';
          this.loading = false;
          console.error(err);
        }
      });
    }
  }

  getStockStatus(): string {
    if (!this.product) return '';
    if (this.product.stockQuantity === 0) return 'out-of-stock';
    if (this.product.stockQuantity < 20) return 'low-stock';
    return 'in-stock';
  }

  getStockLabel(): string {
    if (!this.product) return '';
    if (this.product.stockQuantity === 0) return 'Out of Stock';
    if (this.product.stockQuantity < 20) return `Only ${this.product.stockQuantity} left!`;
    return 'In Stock';
  }

  incrementQuantity() {
    if (this.quantity < this.product.stockQuantity) {
      this.quantity++;
    }
  }

  decrementQuantity() {
    if (this.quantity > 1) {
      this.quantity--;
    }
  }

  addToCart() {
    if (!this.authService.isLoggedIn()) {
      this.toastService.show('Please login to add items to cart', 'warning');
      this.router.navigate(['/login']);
      return;
    }
    
    if (this.product.stockQuantity === 0) {
      this.toastService.show('This product is out of stock', 'danger');
      return;
    }

    this.cartService.addToCart(this.product.id, this.quantity).subscribe({
      next: () => {
        this.toastService.show(`Added ${this.quantity} item(s) to cart!`, 'success');
      },
      error: (err) => {
        console.error('Error adding to cart:', err);
        if (err.status === 401 || err.status === 403) {
            this.toastService.show('Session expired or Permission Denied. Please check logs.', 'warning');
            // this.authService.logout();
            // this.router.navigate(['/login']);
        } else {
            this.toastService.show('Failed to add to cart. Please try again.', 'danger');
        }
      }
    });
  }

  goBack() {
    this.router.navigate(['/products']);
  }
}
