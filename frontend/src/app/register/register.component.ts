import { Component } from '@angular/core';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';
import { ToastService } from '../toast.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  user = {
    firstName: '',
    lastName: '',
    email: '',
    password: '',
    phoneNumber: '',
    address: '',
    role: 'USER'
  };
  error = '';

  constructor(
    private authService: AuthService, 
    private router: Router,
    private toastService: ToastService
  ) { }


  register() {
    this.authService.register(this.user).subscribe({
      next: () => {
        this.toastService.show('Registration successful! Please login.', 'success');
        this.router.navigate(['/login']);
      },

      error: err => {
        this.error = 'Registration failed. Try again.';
        console.error(err);
      }
    });
  }
}
