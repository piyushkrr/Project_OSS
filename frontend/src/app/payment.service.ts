import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface PaymentRequest {
    orderId: number;
    paymentMethod: string; // 'CREDIT_CARD', 'DEBIT_CARD', 'UPI', 'NET_BANKING'
    amount: number;
    // Mock card details
    cardNumber?: string;
    cardHolderName?: string;
    expiryDate?: string;
    cvv?: string;
    // Mock UPI
    upiId?: string;
}

@Injectable({
    providedIn: 'root'
})
export class PaymentService {
    private apiUrl = 'http://localhost:8181/api/payments';
    private savedApiUrl = 'http://localhost:8181/api/payments/saved';

    constructor(private http: HttpClient) { }

    processPayment(request: PaymentRequest): Observable<any> {
        return this.http.post(`${this.apiUrl}/process`, request);
    }

    getSavedPaymentMethods(): Observable<any[]> {
        return this.http.get<any[]>(this.savedApiUrl);
    }

    savePaymentMethod(method: any): Observable<any> {
        return this.http.post(this.savedApiUrl, method);
    }

    deleteSavedPaymentMethod(id: number): Observable<void> {
        return this.http.delete<void>(`${this.savedApiUrl}/${id}`);
    }

    updateSavedPaymentMethod(id: number, method: any): Observable<any> {
        return this.http.put<any>(`${this.savedApiUrl}/${id}`, method);
    }
}
