import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class AddressService {
    private apiUrl = 'http://localhost:8181/api/addresses';

    constructor(private http: HttpClient) { }

    getUserAddresses(): Observable<any[]> {
        return this.http.get<any[]>(this.apiUrl);
    }

    addAddress(address: any): Observable<any> {
        return this.http.post(this.apiUrl, address);
    }

    deleteAddress(id: number): Observable<any> {
        return this.http.delete(`${this.apiUrl}/${id}`);
    }
}
