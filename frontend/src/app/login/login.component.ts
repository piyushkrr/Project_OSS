import { Component } from '@angular/core';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';
import { ToastService } from '../toast.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  credentials = { email: '', password: '' };
  error = '';

  constructor(
    private authService: AuthService, 
    private router: Router,
    private toastService: ToastService
  ) { }


  login() {
    this.authService.login(this.credentials).subscribe({
      next: () => {
        this.toastService.show('Welcome back!', 'success');
        this.router.navigate(['/products']);
      },

      error: err => {
        this.error = 'Invalid email or password';
        console.error(err);
      }
    });
  }
}
