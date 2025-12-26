import { Component, OnInit } from '@angular/core';
import { ProductService } from '../product.service';
import { CartService } from '../cart.service';
import { AuthService } from '../auth.service';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastService } from '../toast.service';

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.css']
})
export class ProductListComponent implements OnInit {
  products: any[] = [];
  allProducts: any[] = [];
  searchQuery: string = '';
  selectedCategory: string = '';
  loading: boolean = true;
  errorMessage: string = '';

  // Sort and Filter properties
  sortBy: string = '';
  minPrice: number = 0;
  maxPrice: number = 10000;
  priceRange: { min: number; max: number } = { min: 0, max: 10000 };
  inStockOnly: boolean = false;

  constructor(
    private productService: ProductService,
    private cartService: CartService,
    public authService: AuthService,
    private route: ActivatedRoute,
    private router: Router,
    private toastService: ToastService
  ) { }

  ngOnInit(): void {
    this.productService.getProducts().subscribe({
      next: (data) => {
        this.allProducts = data;
        this.calculatePriceRange();
        this.route.queryParams.subscribe(params => {
          this.selectedCategory = params['category'] || '';
          this.searchQuery = params['search'] || '';
          this.sortBy = params['sort'] || '';
          this.applyFilters();
          this.loading = false;
        });
      },
      error: (err) => {
        this.errorMessage = 'Failed to load products. Please try again later.';
        this.loading = false;
        console.error(err);
      }
    });
  }

  calculatePriceRange() {
    if (this.allProducts.length > 0) {
      const prices = this.allProducts.map(p => parseFloat(p.price));
      this.priceRange.min = Math.floor(Math.min(...prices));
      this.priceRange.max = Math.ceil(Math.max(...prices));
      this.minPrice = this.priceRange.min;
      this.maxPrice = this.priceRange.max;
    }
  }

  applyFilters() {
    let filtered = [...this.allProducts];

    // Category filter
    if (this.selectedCategory) {
      filtered = filtered.filter(p => p.category === this.selectedCategory);
    }

    // Search filter
    if (this.searchQuery) {
      const query = this.searchQuery.toLowerCase();
      filtered = filtered.filter(p =>
        p.name.toLowerCase().includes(query) ||
        p.description.toLowerCase().includes(query) ||
        p.category.toLowerCase().includes(query)
      );
    }

    // Price range filter
    filtered = filtered.filter(p => {
      const price = parseFloat(p.price);
      return price >= this.minPrice && price <= this.maxPrice;
    });

    // Stock filter
    if (this.inStockOnly) {
      filtered = filtered.filter(p => p.stockQuantity > 0);
    }

    // Apply sorting
    this.applySorting(filtered);
  }

  applySorting(products: any[]) {
    switch (this.sortBy) {
      case 'price-asc':
        products.sort((a, b) => parseFloat(a.price) - parseFloat(b.price));
        break;
      case 'price-desc':
        products.sort((a, b) => parseFloat(b.price) - parseFloat(a.price));
        break;
      case 'name-asc':
        products.sort((a, b) => a.name.localeCompare(b.name));
        break;
      case 'name-desc':
        products.sort((a, b) => b.name.localeCompare(a.name));
        break;
      case 'newest':
        products.sort((a, b) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime());
        break;
      case 'popular':
        products.sort((a, b) => (b.isPopular ? 1 : 0) - (a.isPopular ? 1 : 0) || (b.rating || 0) - (a.rating || 0));
        break;
      default:
        // No sorting
        break;
    }

    this.products = products;
  }

  onSortChange() {
    this.applyFilters();
  }

  onPriceRangeChange() {
    this.applyFilters();
  }

  onStockFilterChange() {
    this.applyFilters();
  }

  resetFilters() {
    this.minPrice = this.priceRange.min;
    this.maxPrice = this.priceRange.max;
    this.inStockOnly = false;
    this.sortBy = '';
    this.applyFilters();
  }

  shopNow() {
    console.log('Shop Now clicked - sorting by popular');
    this.sortBy = 'popular';
    this.applyFilters();
    // Wait for Angular to update the DOM after sorting
    setTimeout(() => {
      console.log('Attempting to scroll...');
      this.scrollToProducts();
    }, 300);
  }

  showNewest() {
    console.log('New Arrivals clicked - sorting by newest');
    this.sortBy = 'newest';
    this.applyFilters();
    // Wait for Angular to update the DOM after sorting
    setTimeout(() => {
      console.log('Attempting to scroll...');
      this.scrollToProducts();
    }, 300);
  }

  private scrollToProducts() {
    console.log('scrollToProducts called');

    // Try to find the hero banner and scroll past it
    const hero = document.querySelector('.hero-banner');
    if (hero) {
      const heroHeight = hero.clientHeight;
      console.log('Hero height:', heroHeight);
      // Scroll further to show product cards directly (not just header)
      window.scrollTo({
        top: heroHeight + 200, // Extra 200px to get past filters and show products
        behavior: 'smooth'
      });
      console.log('Scroll executed');
    } else {
      // Fallback: scroll to products section
      const element = document.getElementById('products-section');
      console.log('Products section element:', element);
      if (element) {
        const elementPosition = element.getBoundingClientRect().top + window.pageYOffset;
        const offsetPosition = elementPosition + 150; // Scroll a bit more to show products
        console.log('Scrolling to position:', offsetPosition);

        window.scrollTo({
          top: offsetPosition,
          behavior: 'smooth'
        });
      } else {
        console.log('Fallback: scrolling to fixed position');
        window.scrollTo({
          top: 700,
          behavior: 'smooth'
        });
      }
    }
  }

  getActiveFilters(): Array<{ label: string, type: string }> {
    const filters: Array<{ label: string, type: string }> = [];

    if (this.selectedCategory) {
      filters.push({ label: `Category: ${this.selectedCategory}`, type: 'category' });
    }

    if (this.minPrice > this.priceRange.min || this.maxPrice < this.priceRange.max) {
      filters.push({ label: `Price: ₹${this.minPrice} - ₹${this.maxPrice}`, type: 'price' });
    }

    if (this.inStockOnly) {
      filters.push({ label: 'In Stock Only', type: 'stock' });
    }

    if (this.searchQuery) {
      filters.push({ label: `Search: "${this.searchQuery}"`, type: 'search' });
    }

    return filters;
  }

  removeFilter(filterType: string) {
    switch (filterType) {
      case 'category':
        this.selectedCategory = '';
        break;
      case 'price':
        this.minPrice = this.priceRange.min;
        this.maxPrice = this.priceRange.max;
        break;
      case 'stock':
        this.inStockOnly = false;
        break;
      case 'search':
        this.searchQuery = '';
        break;
    }
    this.applyFilters();
  }

  addToCart(product: any) {
    console.log('Add to Cart clicked for product:', product);
    console.log('Is logged in?', this.authService.isLoggedIn());

    if (!this.authService.isLoggedIn()) {
      console.log('User not logged in, redirecting to login');
      this.toastService.show('Please login to add items to cart', 'warning');
      setTimeout(() => this.router.navigate(['/login']), 1500);
      return;
    }

    console.log('Attempting to add product to cart...');
    this.cartService.addToCart(product.id, 1).subscribe({
      next: () => {
        console.log('Successfully added to cart');
        this.toastService.show('Added to cart!', 'success');
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
}

