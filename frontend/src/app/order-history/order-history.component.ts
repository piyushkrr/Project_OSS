import { Component, OnInit } from '@angular/core';
import { OrderService } from '../order.service';
import { PaymentService } from '../payment.service';
import { Router } from '@angular/router';
import { ToastService } from '../toast.service';

@Component({
  selector: 'app-order-history',
  templateUrl: './order-history.component.html',
  styleUrls: ['./order-history.component.css']
})
export class OrderHistoryComponent implements OnInit {
  allOrders: any[] = [];
  orders: any[] = [];
  loading = true;
  errorMessage = '';
  processingId: number | null = null;

  statusFilter: string = '';
  orderSearchQuery: string = '';

  constructor(
    private orderService: OrderService,
    private paymentService: PaymentService,
    private router: Router,
    private toastService: ToastService
  ) { }

  ngOnInit(): void {
    this.loadOrders();
  }

  loadOrders() {
    this.loading = true;
    this.orderService.getUserOrders().subscribe({
      next: (data) => {
        this.allOrders = [...data].sort((a, b) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime());
        this.applyFilters();
        this.loading = false;
      },
      error: (err) => {
        console.error('Full Error Object:', err);
        this.errorMessage = `Failed to load order history. Status: ${err.status}, Message: ${err.message}`;
        this.loading = false;
      }
    });
  }

  applyFilters() {
    this.orders = this.allOrders.filter(order => {
      const matchesStatus = !this.statusFilter || order.status === this.statusFilter;
      const matchesSearch = !this.orderSearchQuery ||
        order.orderTrackingId?.toLowerCase().includes(this.orderSearchQuery.toLowerCase()) ||
        order.shippingAddress?.toLowerCase().includes(this.orderSearchQuery.toLowerCase()) ||
        order.orderItems?.some((item: any) => item.product.name.toLowerCase().includes(this.orderSearchQuery.toLowerCase()));

      return matchesStatus && matchesSearch;
    });
  }

  payNow(order: any) {
    this.processingId = order.id;
    // For simplicity, we navigate to checkout but we need a way to tell it WHICH order.
    // Or we just call paymentService.processPayment directly since we have the order details.
    // Let's call it directly to make it faster for the user.

    // We don't have the card details here, so we'll just mock it as 'SAVED_METHOD'
    this.paymentService.processPayment({
      orderId: order.id,
      amount: order.totalAmount,
      paymentMethod: 'SAVED_METHOD'
    }).subscribe({
      next: (res) => {
        this.toastService.show('Payment successful!', 'success');
        this.processingId = null;
        this.loadOrders(); // Refresh status
      },
      error: (err) => {
        console.error('Payment Error Details:', err);
        const errorMsg = err.error?.message || err.message || 'Unknown error';
        this.toastService.show(`Payment failed: ${errorMsg}. Please try again.`, 'danger');
        this.processingId = null;
      }
    });
  }
}
