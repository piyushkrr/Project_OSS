import { Component, OnInit } from '@angular/core';
import { OrderService } from '../../order.service';
import { ToastService } from '../../toast.service';

@Component({
  selector: 'app-admin-order-list',
  templateUrl: './admin-order-list.component.html',
  styleUrls: ['./admin-order-list.component.css']
})
export class AdminOrderListComponent implements OnInit {
  orders: any[] = [];
  loading = true;
  errorMessage = '';
  statusOptions = ['PENDING', 'SHIPPED', 'DELIVERED', 'CANCELLED'];

  constructor(
    private orderService: OrderService,
    private toastService: ToastService
  ) { }

  ngOnInit(): void {
    this.loadOrders();
  }

  loadOrders() {
    this.loading = true;
    this.orderService.getAllOrders().subscribe({
      next: (data) => {
        this.orders = data;
        this.loading = false;
      },
      error: (err) => {
        this.errorMessage = 'Failed to load customer orders.';
        this.loading = false;
        console.error(err);
      }
    });
  }

  updateStatus(orderId: number, status: string) {
    this.orderService.updateOrderStatus(orderId, status).subscribe({
      next: () => {
        this.toastService.show('Order status updated successfully', 'success');
        this.loadOrders();
      },
      error: (err) => {
        this.toastService.show('Failed to update order status', 'danger');
        console.error(err);
      }
    });
  }
}
