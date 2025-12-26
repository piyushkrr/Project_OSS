import { Injectable } from '@angular/core';

export interface Toast {
  message: string;
  type: 'success' | 'danger' | 'info' | 'warning';
}

@Injectable({
  providedIn: 'root'
})
export class ToastService {
  toasts: Toast[] = [];

  show(message: string, type: 'success' | 'danger' | 'info' | 'warning' = 'info') {
    const toast: Toast = { message, type };
    this.toasts.push(toast);
    
    // Auto remove after 3 seconds
    setTimeout(() => {
      this.remove(toast);
    }, 3000);
  }

  remove(toast: Toast) {
    this.toasts = this.toasts.filter(t => t !== toast);
  }
}
