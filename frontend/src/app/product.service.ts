import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  private apiUrl = 'http://localhost:8181/api/products';

  constructor(private http: HttpClient) { }

  getProducts(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }

  getProductById(id: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/${id}`);
  }

  createProduct(product: any, images: File[]): Observable<any> {
    const formData = new FormData();
    formData.append('product', JSON.stringify(product));
    
    if (images && images.length > 0) {
      images.forEach(image => {
        formData.append('images', image);
      });
    }

    return this.http.post(this.apiUrl, formData);
  }

  updateProduct(id: number, product: any, images: File[]): Observable<any> {
    const formData = new FormData();
    formData.append('product', JSON.stringify(product));
    
    if (images && images.length > 0) {
      images.forEach(image => {
        formData.append('images', image);
      });
    }

    return this.http.put(`${this.apiUrl}/${id}`, formData);
  }

  deleteProduct(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }
}


