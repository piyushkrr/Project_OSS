import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject, tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CartService {

  private apiUrl = 'http://localhost:8181/api/cart';
  private cartSubject = new BehaviorSubject<any>(null);
  cart$ = this.cartSubject.asObservable();

  constructor(private http: HttpClient) { }

  getCart(): Observable<any> {
    return this.http.get<any>(this.apiUrl).pipe(
      tap(cart => this.cartSubject.next(cart))
    );
  }

  addToCart(productId: number, quantity: number): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/add`, { productId, quantity }).pipe(
      tap(cart => this.cartSubject.next(cart))
    );
  }

  removeFromCart(productId: number): Observable<any> {
    return this.http.delete<any>(`${this.apiUrl}/remove/${productId}`).pipe(
      tap(cart => this.cartSubject.next(cart))
    );
  }

  clearCart(): Observable<void> {
    return this.http.delete<void>(this.apiUrl).pipe(
        tap(() => this.cartSubject.next(null)) 
    );
  }
}
