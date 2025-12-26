import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class OrderService {
  private apiUrl = 'http://localhost:8181/api/orders';

  constructor(private http: HttpClient) { }

  placeOrder(orderRequest: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/place`, orderRequest);
  }

  getUserOrders(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }

  getOrder(orderId: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/${orderId}`);
  }

  getAllOrders(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/admin/all`);
  }

  updateOrderStatus(orderId: number, status: string): Observable<any> {
    return this.http.put(`${this.apiUrl}/admin/${orderId}/status?status=${status}`, {});
  }
}

