import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AdminOrderListComponent } from './admin-order-list.component';
import { OrderService } from '../../order.service';
import { of, throwError } from 'rxjs';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormsModule } from '@angular/forms';

describe('AdminOrderListComponent', () => {
  let component: AdminOrderListComponent;
  let fixture: ComponentFixture<AdminOrderListComponent>;
  let orderService: jasmine.SpyObj<OrderService>;

  beforeEach(async () => {
    const spy = jasmine.createSpyObj('OrderService', ['getAllOrders', 'updateOrderStatus']);
    
    await TestBed.configureTestingModule({
      declarations: [ AdminOrderListComponent ],
      imports: [ HttpClientTestingModule, FormsModule ],
      providers: [
        { provide: OrderService, useValue: spy }
      ]
    }).compileComponents();

    orderService = TestBed.get(OrderService);
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminOrderListComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    orderService.getAllOrders.and.returnValue(of([]));
    fixture.detectChanges();
    expect(component).toBeTruthy();
  });

  it('should load orders on init', () => {
    const mockOrders = [{ id: 1, totalAmount: 100, status: 'PENDING', user: { firstName: 'John' } }];
    orderService.getAllOrders.and.returnValue(of(mockOrders));
    fixture.detectChanges();
    expect(component.orders.length).toBe(1);
    expect(component.loading).toBeFalse();
  });

  it('should handle error when loading orders', () => {
    orderService.getAllOrders.and.returnValue(throwError({ status: 500 }));
    fixture.detectChanges();
    expect(component.errorMessage).toBe('Failed to load customer orders.');
    expect(component.loading).toBeFalse();
  });

  it('should update order status', () => {
    spyOn(window, 'alert');
    orderService.updateOrderStatus.and.returnValue(of({}));
    orderService.getAllOrders.and.returnValue(of([]));
    
    component.updateStatus(1, 'SHIPPED');
    
    expect(orderService.updateOrderStatus).toHaveBeenCalledWith(1, 'SHIPPED');
    expect(window.alert).toHaveBeenCalledWith('Order status updated successfully');
  });
});
