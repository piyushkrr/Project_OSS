import { Component } from '@angular/core';
import { ToastService, Toast } from '../toast.service';

@Component({
  selector: 'app-toasts',
  template: `
    <div class="toast-container position-fixed bottom-0 end-0 p-3" style="z-index: 1200">
      <div *ngFor="let toast of toastService.toasts" 
           class="toast show align-items-center border-0 mb-2 shadow animate-slide-in" 
           [ngClass]="'text-white bg-' + toast.type" 
           role="alert" aria-live="assertive" aria-atomic="true">
        <div class="d-flex">
          <div class="toast-body">
            <i class="bi me-2" [ngClass]="getIcon(toast.type)"></i>
            {{ toast.message }}
          </div>
          <button type="button" class="btn-close btn-close-white me-2 m-auto" (click)="toastService.remove(toast)"></button>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .animate-slide-in {
      animation: slideIn 0.3s ease-out;
    }
    @keyframes slideIn {
      from { transform: translateX(100%); opacity: 0; }
      to { transform: translateX(0); opacity: 1; }
    }
  `]
})
export class ToastComponent {
  constructor(public toastService: ToastService) {}

  getIcon(type: string) {
    switch (type) {
      case 'success': return 'bi-check-circle-fill';
      case 'danger': return 'bi-exclamation-triangle-fill';
      case 'warning': return 'bi-exclamation-circle-fill';
      default: return 'bi-info-circle-fill';
    }
  }
}
