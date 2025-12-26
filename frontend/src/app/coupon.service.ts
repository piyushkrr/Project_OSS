import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { map, catchError } from 'rxjs/operators';

export interface Coupon {
    id?: number;
    code: string;
    minOrderValue: number;
    discountAmount: number;
    description: string;
    isActive?: boolean;
    expiryDate?: string;
    usageLimit?: number;
    usedCount?: number;
}

@Injectable({
    providedIn: 'root'
})
export class CouponService {
    private apiUrl = 'http://localhost:8181/api/coupons';
    private cachedCoupons: Coupon[] | null = null;

    constructor(private http: HttpClient) { }

    private getAuthHeaders(): HttpHeaders {
        const token = localStorage.getItem('token');
        return new HttpHeaders({
            'Authorization': `Bearer ${token}`
        });
    }

    getActiveCoupons(): Observable<Coupon[]> {
        return this.http.get<Coupon[]>(`${this.apiUrl}/active`).pipe(
            catchError(err => {
                console.error('Error fetching coupons:', err);
                return of([]);
            })
        );
    }

    validateCoupon(code: string, orderTotal: number): { valid: boolean; coupon?: Coupon; message: string } {
        // Client-side validation is deprecated - use backend validation
        // This method is kept for backward compatibility but should use backend
        console.warn('Using deprecated client-side validation. Use validateCouponBackend instead.');

        if (!this.cachedCoupons) {
            return {
                valid: false,
                message: 'Coupons not loaded'
            };
        }

        const coupon = this.cachedCoupons.find(c => c.code.toUpperCase() === code.toUpperCase());

        if (!coupon) {
            return {
                valid: false,
                message: 'Invalid coupon code'
            };
        }

        if (orderTotal < coupon.minOrderValue) {
            return {
                valid: false,
                message: `Coupon requires minimum order value of ₹${coupon.minOrderValue}`
            };
        }

        return {
            valid: true,
            coupon: coupon,
            message: `Coupon applied! You saved ₹${coupon.discountAmount}`
        };
    }

    validateCouponBackend(code: string, orderTotal: number): Observable<any> {
        return this.http.post<any>(`${this.apiUrl}/validate`, {
            code: code,
            orderTotal: orderTotal
        });
    }

    getAllCoupons(): Coupon[] {
        return this.cachedCoupons || [];
    }

    loadCoupons(): void {
        this.getActiveCoupons().subscribe(coupons => {
            this.cachedCoupons = coupons;
        });
    }
}
