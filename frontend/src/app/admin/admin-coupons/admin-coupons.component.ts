import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { ToastService } from '../../toast.service';

interface Coupon {
  id?: number;
  code: string;
  description: string;
  minOrderValue: number;
  discountAmount: number;
  isActive: boolean;
  expiryDate?: string;
  usageLimit?: number;
  usedCount?: number;
}

@Component({
  selector: 'app-admin-coupons',
  templateUrl: './admin-coupons.component.html',
  styleUrls: ['./admin-coupons.component.css']
})
export class AdminCouponsComponent implements OnInit {
  coupons: Coupon[] = [];
  loading = false;
  showForm = false;
  editingCoupon: Coupon | null = null;

  formData: Coupon = {
    code: '',
    description: '',
    minOrderValue: 0,
    discountAmount: 0,
    isActive: true
  };

  private apiUrl = 'http://localhost:8181/api/coupons';

  constructor(
    private http: HttpClient,
    private toastService: ToastService
  ) { }

  ngOnInit(): void {
    this.loadCoupons();
  }

  loadCoupons(): void {
    this.loading = true;
    const headers = this.getAuthHeaders();

    this.http.get<Coupon[]>(this.apiUrl, { headers }).subscribe({
      next: (data) => {
        this.coupons = data;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error loading coupons:', err);
        this.toastService.show('Failed to load coupons', 'danger');
        this.loading = false;
      }
    });
  }

  openAddForm(): void {
    this.editingCoupon = null;
    this.formData = {
      code: '',
      description: '',
      minOrderValue: 0,
      discountAmount: 0,
      isActive: true
    };
    this.showForm = true;
  }

  openEditForm(coupon: Coupon): void {
    this.editingCoupon = coupon;
    this.formData = { ...coupon };
    this.showForm = true;
  }

  closeForm(): void {
    this.showForm = false;
    this.editingCoupon = null;
  }

  saveCoupon(): void {
    const headers = this.getAuthHeaders();

    if (this.editingCoupon) {
      // Update existing coupon
      this.http.put<Coupon>(`${this.apiUrl}/${this.editingCoupon.id}`, this.formData, { headers })
        .subscribe({
          next: () => {
            this.toastService.show('Coupon updated successfully', 'success');
            this.loadCoupons();
            this.closeForm();
          },
          error: (err) => {
            console.error('Error updating coupon:', err);
            this.toastService.show(err.error?.error || 'Failed to update coupon', 'danger');
          }
        });
    } else {
      // Create new coupon
      this.http.post<Coupon>(this.apiUrl, this.formData, { headers })
        .subscribe({
          next: () => {
            this.toastService.show('Coupon created successfully', 'success');
            this.loadCoupons();
            this.closeForm();
          },
          error: (err) => {
            console.error('Error creating coupon:', err);
            this.toastService.show(err.error?.error || 'Failed to create coupon', 'danger');
          }
        });
    }
  }

  deleteCoupon(coupon: Coupon): void {
    if (!confirm(`Are you sure you want to delete coupon "${coupon.code}"?`)) {
      return;
    }

    const headers = this.getAuthHeaders();

    this.http.delete(`${this.apiUrl}/${coupon.id}`, { headers }).subscribe({
      next: () => {
        this.toastService.show('Coupon deleted successfully', 'success');
        this.loadCoupons();
      },
      error: (err) => {
        console.error('Error deleting coupon:', err);
        this.toastService.show('Failed to delete coupon', 'danger');
      }
    });
  }

  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
  }
}
