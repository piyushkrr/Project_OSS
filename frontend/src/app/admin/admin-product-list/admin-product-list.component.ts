import { Component, OnInit } from '@angular/core';
import { ProductService } from '../../product.service';
import { ToastService } from '../../toast.service';

@Component({
  selector: 'app-admin-product-list',
  templateUrl: './admin-product-list.component.html',
  styleUrls: ['./admin-product-list.component.css']
})
export class AdminProductListComponent implements OnInit {
  products: any[] = [];
  loading = true;
  errorMessage = '';

  constructor(
    private productService: ProductService,
    private toastService: ToastService
  ) { }

  ngOnInit(): void {
    this.loadProducts();
  }

  loadProducts() {
    this.loading = true;
    this.productService.getProducts().subscribe({
      next: (data) => {
        this.products = data;
        this.loading = false;
      },
      error: (err) => {
        this.errorMessage = 'Failed to load products.';
        this.loading = false;
        console.error(err);
      }
    });
  }

  deleteProduct(id: number) {
    if (confirm('Are you sure you want to delete this product?')) {
      this.productService.deleteProduct(id).subscribe({
        next: () => {
          this.toastService.show('Product deleted successfully', 'info');
          this.loadProducts();
        },
        error: (err) => {
          this.toastService.show('Failed to delete product', 'danger');
          console.error(err);
        }
      });
    }
  }
}
