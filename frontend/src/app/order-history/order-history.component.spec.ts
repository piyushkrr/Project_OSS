import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { OrderHistoryComponent } from './order-history.component';
import { OrderService } from '../order.service';
import { of } from 'rxjs';

describe('OrderHistoryComponent', () => {
  let component: OrderHistoryComponent;
  let fixture: ComponentFixture<OrderHistoryComponent>;
  let orderService: OrderService;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ OrderHistoryComponent ],
      imports: [ HttpClientTestingModule ],
      providers: [ OrderService ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(OrderHistoryComponent);
    component = fixture.componentInstance;
    orderService = TestBed.inject(OrderService);

    // Mock getUserOrders
    spyOn(orderService, 'getUserOrders').and.returnValue(of([]));

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
