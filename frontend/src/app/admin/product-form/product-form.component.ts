import { Component, OnInit } from '@angular/core';
import { ProductService } from '../../product.service';
import { Router, ActivatedRoute } from '@angular/router';
import { ToastService } from '../../toast.service';

@Component({
  selector: 'app-product-form',
  templateUrl: './product-form.component.html',
  styleUrls: ['./product-form.component.css']
})
export class ProductFormComponent implements OnInit {
  product: any = {
    name: '',
    description: '',
    price: 0,
    category: 'Electronics',
    stockQuantity: 0
  };
  selectedImages: File[] = [];
  imagePreviews: string[] = [];
  isEditMode = false;
  processing = false;
  errorMessage = '';


  constructor(
    private productService: ProductService,
    private router: Router,
    private route: ActivatedRoute,
    private toastService: ToastService
  ) { }



  ngOnInit(): void {
    const id = this.route.snapshot.params['id'];
    if (id) {
      this.isEditMode = true;
      this.productService.getProductById(id).subscribe({
        next: (data) => {
          this.product = data;
        },
        error: (err) => {
          this.errorMessage = 'Product not found or failed to load.';
          console.error(err);
        }
      });
    }

  }

  onFileChange(event: any) {
    this.selectedImages = Array.from(event.target.files);
    this.imagePreviews = [];
    this.selectedImages.forEach(file => {
      const reader = new FileReader();
      reader.onload = (e: any) => {
        this.imagePreviews.push(e.target.result);
      };
      reader.readAsDataURL(file);
    });
  }


  saveProduct() {
    this.processing = true;

    const saveObs = this.isEditMode
      ? this.productService.updateProduct(this.product.id, this.product, this.selectedImages)
      : this.productService.createProduct(this.product, this.selectedImages);

    saveObs.subscribe({
      next: () => {
        this.toastService.show(this.isEditMode ? 'Product updated successfully!' : 'Product created successfully!', 'success');
        this.router.navigate(['/admin/products']);
      },
      error: (err) => {
        this.toastService.show('Failed to save product.', 'danger');
        console.error(err);
        this.processing = false;
      }
    });

  }

}
