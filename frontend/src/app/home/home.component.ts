import { Component, OnInit } from '@angular/core';
import { ProductService } from '../product.service';
import { CouponService } from '../coupon.service';
import { Router } from '@angular/router';

@Component({
    selector: 'app-home',
    templateUrl: './home.component.html',
    styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
    newArrivals: any[] = [];
    popularProducts: any[] = [];
    loading = true;

    constructor(
        private productService: ProductService,
        private couponService: CouponService,
        private router: Router
    ) { }

    ngOnInit(): void {
        this.loadHomeData();
        // Load coupons for display
        this.couponService.loadCoupons();
    }

    loadHomeData(): void {
        this.productService.getProducts().subscribe({
            next: (products) => {
                // New Arrivals: Latest 4 products (by createdAt desc)
                this.newArrivals = [...products]
                    .sort((a, b) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime())
                    .slice(0, 4);

                // Popular Products: Filtered by isPopular flag, limit 4
                this.popularProducts = products
                    .filter(p => p.isPopular)
                    .slice(0, 4);

                this.loading = false;
            },
            error: (err) => {
                console.error('Error loading products for home', err);
                this.loading = false;
            }
        });
    }

    shopNow(): void {
        this.router.navigate(['/products'], { queryParams: { sort: 'popular' } });
    }

    browseCategory(category: string): void {
        this.router.navigate(['/products'], { queryParams: { category: category } });
    }
}
