import { Component, OnInit } from '@angular/core';
import { AuthService } from '../auth.service';
import { AddressService } from '../address.service';
import { PaymentService } from '../payment.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  activeTab: 'info' | 'orders' | 'addresses' | 'payments' = 'info';

  // User Info
  user: any = {};
  editMode = false;
  processing = false;

  // Addresses
  addresses: any[] = [];
  newAddress: any = {
    street: '',
    city: '',
    state: '',
    zipCode: '',
    country: '',
    label: 'Home'
  };
  showAddressForm = false;
  addressProcessing = false;

  // Payments
  savedPayments: any[] = [];
  showPaymentForm = false;
  paymentProcessing = false;
  paymentMethodType: 'CREDIT_CARD' | 'DEBIT_CARD' | 'UPI' | 'NET_BANKING' | null = null;
  newPaymentDetails: any = {
    cardNumber: '', cardHolderName: '', expiryDate: '', cvv: '', upiId: ''
  };

  // Feedback
  message = '';
  isError = false;

  constructor(
    private authService: AuthService,
    private addressService: AddressService,
    private paymentService: PaymentService
  ) { }

  ngOnInit(): void {
    this.loadProfile();
    this.loadAddresses();
    this.loadSavedPayments();
  }

  setActiveTab(tab: 'info' | 'orders' | 'addresses' | 'payments') {
    this.activeTab = tab;
    this.message = '';
  }

  loadProfile() {
    this.authService.getProfile().subscribe({
      next: (data: any) => {
        this.user = data;
        this.user.password = '';
      },
      error: (err: any) => {
        console.error(err);
      }
    });
  }

  toggleEdit() {
    this.editMode = !this.editMode;
    this.message = '';
  }

  saveProfile() {
    this.processing = true;
    this.message = '';
    this.authService.updateProfile(this.user).subscribe({
      next: (updatedUser: any) => {
        this.user = updatedUser;
        this.user.password = '';
        this.editMode = false;
        this.processing = false;
        this.message = 'Profile updated successfully!';
        this.isError = false;
      },
      error: (err: any) => {
        this.message = 'Failed to update profile.';
        this.isError = true;
        this.processing = false;
        console.error(err);
      }
    });
  }

  // Address Methods
  loadAddresses() {
    this.addressService.getUserAddresses().subscribe({
      next: (data: any) => {
        this.addresses = data;
      },
      error: (err: any) => console.error(err)
    });
  }

  toggleAddressForm() {
    this.showAddressForm = !this.showAddressForm;
  }

  saveAddress() {
    this.addressProcessing = true;
    this.addressService.addAddress(this.newAddress).subscribe({
      next: (address: any) => {
        this.addresses.push(address);
        this.showAddressForm = false;
        this.newAddress = { street: '', city: '', state: '', zipCode: '', country: '', label: 'Home' }; // Reset
        this.addressProcessing = false;
        this.message = 'Address added successfully!';
        this.isError = false;
      },
      error: (err: any) => {
        console.error(err);
        this.addressProcessing = false;
        this.message = 'Failed to add address.';
        this.isError = true;
      }
    });
  }

  deleteAddress(id: number) {
    if (confirm('Are you sure you want to delete this address?')) {
      this.addressService.deleteAddress(id).subscribe({
        next: () => {
          this.addresses = this.addresses.filter(a => a.id !== id);
          this.message = 'Address deleted.';
          this.isError = false;
        },
        error: (err: any) => {
          console.error(err);
          this.message = 'Failed to delete address.';
          this.isError = true;
        }
      });
    }
  }

  // Payment Methods
  loadSavedPayments() {
    this.paymentService.getSavedPaymentMethods().subscribe({
      next: (data: any[]) => {
        this.savedPayments = data;
      },
      error: (err: any) => console.error(err)
    });
  }

  togglePaymentForm() {
    this.showPaymentForm = !this.showPaymentForm;
    this.paymentMethodType = null;
    this.editingPaymentId = null;
    this.newPaymentDetails = { cardNumber: '', cardHolderName: '', expiryDate: '', cvv: '', upiId: '' };
  }

  savePaymentMethod() {
    this.paymentProcessing = true;
    this.message = '';
    this.isError = false;

    // Prepare payload matching SavedPaymentMethod entity
    let payload: any = {
      type: '',
      provider: '',
      maskedNumber: ''
    };

    if (this.paymentMethodType === 'CREDIT_CARD' || this.paymentMethodType === 'DEBIT_CARD') {
      const pan = this.newPaymentDetails.cardNumber || '';
      if (pan.length < 4) {
        this.message = 'Invalid Card Number';
        this.isError = true;
        this.paymentProcessing = false;
        return;
      }
      payload.type = 'CARD';
      // Simple provider logic
      if (pan.startsWith('4')) payload.provider = 'Visa';
      else if (pan.startsWith('5')) payload.provider = 'Mastercard';
      else payload.provider = 'Card Network';

      payload.maskedNumber = '**** ' + pan.slice(-4);
      payload.cardHolderName = this.newPaymentDetails.cardHolderName;
      payload.expiryDate = this.newPaymentDetails.expiryDate;

    } else if (this.paymentMethodType === 'UPI') {
      if (!this.newPaymentDetails.upiId) {
        this.message = 'Invalid UPI ID';
        this.isError = true;
        this.paymentProcessing = false;
        return;
      }
      payload.type = 'UPI';
      payload.provider = 'UPI';
      payload.maskedNumber = this.newPaymentDetails.upiId;
    } else {
      this.message = 'This payment method cannot be saved.';
      this.isError = true;
      this.paymentProcessing = false;
      return;
    }

    if (this.editingPaymentId) {
      // Update existing
      this.paymentService.updateSavedPaymentMethod(this.editingPaymentId, payload).subscribe({
        next: (updatedMethod: any) => {
          const index = this.savedPayments.findIndex(p => p.id === this.editingPaymentId);
          if (index !== -1) {
            this.savedPayments[index] = updatedMethod;
          }
          this.closePaymentForm();
          this.message = 'Payment method updated successfully!';
          this.isError = false;
        },
        error: (err: any) => {
          console.error(err);
          this.paymentProcessing = false;
          this.message = 'Failed to update payment method.';
          this.isError = true;
        }
      });
    } else {
      // Create new
      this.paymentService.savePaymentMethod(payload).subscribe({
        next: (method: any) => {
          this.savedPayments.push(method);
          this.closePaymentForm();
          this.message = 'Payment method saved successfully!';
          this.isError = false;
        },
        error: (err: any) => {
          console.error(err);
          this.paymentProcessing = false;
          this.message = 'Failed to save payment method.';
          this.isError = true;
        }
      });
    }
  }

  editingPaymentId: number | null = null;

  editPaymentMethod(method: any) {
    this.showPaymentForm = true;
    this.editingPaymentId = method.id;
    this.paymentMethodType = method.type === 'CARD' ? 'CREDIT_CARD' : method.type; // Map back

    // Pre-fill data
    this.newPaymentDetails = {
      cardNumber: '', // Cannot edit number, show blank or placeholder
      cardHolderName: method.cardHolderName || '',
      expiryDate: method.expiryDate || '',
      cvv: '',
      upiId: method.type === 'UPI' ? method.maskedNumber : ''
    };
  }

  closePaymentForm() {
    this.showPaymentForm = false;
    this.newPaymentDetails = { cardNumber: '', cardHolderName: '', expiryDate: '', cvv: '', upiId: '' };
    this.paymentMethodType = null;
    this.editingPaymentId = null;
    this.paymentProcessing = false;
  }

  deletePaymentMethod(id: number) {
    if (confirm('Are you sure you want to delete this payment method?')) {
      this.paymentService.deleteSavedPaymentMethod(id).subscribe({
        next: () => {
          this.savedPayments = this.savedPayments.filter(p => p.id !== id);
          this.message = 'Payment method deleted.';
          this.isError = false;
        },
        error: (err: any) => {
          console.error(err);
          this.message = 'Failed to delete payment method.';
          this.isError = true;
        }
      });
    }
  }
}