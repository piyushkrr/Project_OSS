import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { FormsModule } from '@angular/forms';
import { CheckoutComponent } from './checkout.component';
import { CartService } from '../cart.service';
import { OrderService } from '../order.service';
import { of } from 'rxjs';

describe('CheckoutComponent', () => {
  let component: CheckoutComponent;
  let fixture: ComponentFixture<CheckoutComponent>;
  let cartService: CartService;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CheckoutComponent ],
      imports: [ 
        HttpClientTestingModule, 
        RouterTestingModule,
        FormsModule
      ],
      providers: [ CartService, OrderService ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CheckoutComponent);
    component = fixture.componentInstance;
    cartService = TestBed.inject(CartService);

    // Mock getCart to return a non-empty cart to stay on page
    spyOn(cartService, 'getCart').and.returnValue(of({ cartItems: [{ product: { id: 1 }, quantity: 1 }] }));

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
