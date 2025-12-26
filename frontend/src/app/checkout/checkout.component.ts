import { Component, OnInit } from '@angular/core';
import { CartService } from '../cart.service';
import { OrderService } from '../order.service';
import { PaymentService, PaymentRequest } from '../payment.service';
import { AddressService } from '../address.service';
import { CouponService } from '../coupon.service';
import { Router } from '@angular/router';
import { ToastService } from '../toast.service';

@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.css']
})
export class CheckoutComponent implements OnInit {
  cart: any = null;
  loading = true;
  errorMessage = '';
  processing = false;

  // Stepper State
  currentStep = 1; // 1: Address, 2: Payment, 3: Review

  // Address State
  savedAddresses: any[] = [];
  selectedAddressId: number | null = null;
  newAddress: any = {
    street: '', city: '', state: '', zipCode: '', country: '', label: 'Home'
  };
  useNewAddress = false;

  // Payment State
  savedPayments: any[] = [];
  selectedPaymentId: number | null = null;
  paymentMethodType: 'CREDIT_CARD' | 'DEBIT_CARD' | 'UPI' | 'NET_BANKING' = 'CREDIT_CARD';
  newPaymentDetails: any = {
    cardNumber: '', cardHolderName: '', expiryDate: '', cvv: '', upiId: ''
  };
  useNewPayment = true;

  // Coupon State
  couponCode: string = '';
  appliedCoupon: any = null;
  couponError: string = '';
  discountAmount: number = 0;

  constructor(
    private cartService: CartService,
    private orderService: OrderService,
    private paymentService: PaymentService,
    private addressService: AddressService,
    private couponService: CouponService,
    private router: Router,
    private toastService: ToastService
  ) { }

  ngOnInit(): void {
    this.loadCart();
    this.loadSavedData();
  }

  loadCart() {
    this.cartService.getCart().subscribe({
      next: (cart) => {
        this.cart = cart;
        this.loading = false;
        if (!cart || !cart.cartItems || cart.cartItems.length === 0) {
          this.toastService.show('Your cart is empty. Redirecting...', 'warning');
          this.router.navigate(['/products']);
        }
      },
      error: (err) => {
        this.errorMessage = 'Failed to load cart.';
        this.loading = false;
      }
    });
  }

  loadSavedData() {
    this.addressService.getUserAddresses().subscribe(
      (data: any[]) => {
        this.savedAddresses = data;
        if (this.savedAddresses.length > 0) {
          this.selectedAddressId = this.savedAddresses[0].id; // Default select first
        } else {
          this.useNewAddress = true;
        }
      }
    );

    this.paymentService.getSavedPaymentMethods().subscribe(
      (data: any[]) => {
        this.savedPayments = data;
        if (this.savedPayments.length > 0) {
          this.selectedPaymentId = this.savedPayments[0].id;
          this.useNewPayment = false;
        }
      }
    );
  }

  // Generic Getters for Template
  get selectedAddress() {
    if (this.useNewAddress) return this.newAddress;
    return this.savedAddresses.find(a => a.id === this.selectedAddressId);
  }

  get formattedShippingAddress(): string {
    const addr = this.selectedAddress;
    if (!addr) return '';
    if (this.useNewAddress) {
      return `${addr.street}, ${addr.city}, ${addr.state} ${addr.zipCode}, ${addr.country}`;
    }
    return `${addr.street}, ${addr.city}, ${addr.state} ${addr.zipCode}, ${addr.country}`;
  }

  get grandTotal(): number {
    if (!this.cart) return 0;
    return this.cart.totalAmount - this.discountAmount;
  }

  applyCoupon() {
    if (!this.couponCode) {
      this.couponError = 'Please enter a coupon code';
      return;
    }

    this.couponService.validateCouponBackend(this.couponCode, this.cart.totalAmount).subscribe({
      next: (response) => {
        if (response.valid) {
          this.appliedCoupon = {
            code: response.code,
            discountAmount: response.discountAmount,
            description: response.description,
            minOrderValue: response.minOrderValue
          };
          this.discountAmount = response.discountAmount;
          this.couponError = '';
          this.toastService.show(response.message || 'Coupon applied successfully', 'success');
        } else {
          this.appliedCoupon = null;
          this.discountAmount = 0;
          this.couponError = response.message;
          this.toastService.show(response.message, 'warning');
        }
      },
      error: (err) => {
        this.appliedCoupon = null;
        this.discountAmount = 0;
        this.couponError = err.error?.message || 'Invalid coupon code';
        this.toastService.show(this.couponError, 'danger');
      }
    });
  }

  removeCoupon() {
    this.appliedCoupon = null;
    this.discountAmount = 0;
    this.couponCode = '';
    this.couponError = '';
    this.toastService.show('Coupon removed', 'info');
  }

  // Action Methods
  proceedToPayment() {
    if (this.useNewAddress) {
      if (!this.newAddress.street || !this.newAddress.city || !this.newAddress.zipCode) {
        this.toastService.show('Please fill in all address fields.', 'warning');
        return;
      }
    } else if (!this.selectedAddressId) {
      this.toastService.show('Please select an address.', 'warning');
      return;
    }
    this.currentStep = 2;
  }

  proceedToReview() {
    // Basic validation for payment
    if (this.useNewPayment) {
      if (this.paymentMethodType === 'UPI' && !this.newPaymentDetails.upiId) {
        this.toastService.show('Please enter UPI ID', 'warning');
        return;
      }
      if ((this.paymentMethodType === 'CREDIT_CARD' || this.paymentMethodType === 'DEBIT_CARD') &&
        (!this.newPaymentDetails.cardNumber || !this.newPaymentDetails.cvv)) {
        this.toastService.show('Please enter card details', 'warning');
        return;
      }
    }
    this.currentStep = 3;
  }

  currentOrderTrackingId: string = '';

  placeOrder() {
    this.processing = true;

    // 1. Create Order
    const phone = '9999999999';

    this.orderService.placeOrder({
      shippingAddress: this.formattedShippingAddress,
      phoneNumber: phone
    }).subscribe({
      next: (order) => {
        this.currentOrderTrackingId = order.orderTrackingId;
        // 2. Process Payment
        this.processPayment(order.id, order.totalAmount);
      },
      error: (err) => {
        this.processing = false;
        this.toastService.show('Failed to create order.', 'danger');
      }
    });
  }

  processPayment(orderId: number, amount: number) {
    const request: PaymentRequest = {
      orderId: orderId,
      amount: amount,
      paymentMethod: this.useNewPayment ? this.paymentMethodType : 'SAVED_METHOD'
    };

    if (this.useNewPayment) {
      request.cardNumber = this.newPaymentDetails.cardNumber;
      request.cardHolderName = this.newPaymentDetails.cardHolderName;
      request.expiryDate = this.newPaymentDetails.expiryDate;
      request.cvv = this.newPaymentDetails.cvv;
      request.upiId = this.newPaymentDetails.upiId;
    }

    this.paymentService.processPayment(request).subscribe({
      next: (payment) => {
        this.processing = false;
        this.toastService.show(`Order placed successfully! ID: ${this.currentOrderTrackingId}`, 'success');

        // Refresh cart to update navbar badge immediately
        this.cartService.getCart().subscribe({
          next: () => {
            console.log('Cart refreshed after order placement');
            this.router.navigate(['/orders']);
          },
          error: (err: any) => {
            console.error('Error refreshing cart:', err);
            // Navigate anyway even if cart refresh fails
            this.router.navigate(['/orders']);
          }
        });
      },
      error: (err) => {
        this.processing = false;
        console.error('Payment Processing Error:', err);
        const errorMsg = err.error?.message || err.message || 'Payment failed';
        this.toastService.show(`Payment failed: ${errorMsg}. Please try again from Order History.`, 'danger');
        // Redirect to orders anyway so they can retry payment there
        this.router.navigate(['/orders']);
      }
    });
  }
}
