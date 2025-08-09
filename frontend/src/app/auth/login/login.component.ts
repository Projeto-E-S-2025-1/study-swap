// src/app/auth/login/login.component.ts
import { Component, OnInit, Inject, PLATFORM_ID } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  loginForm!: FormGroup; // Declare, mas não inicialize no ngOnInit
  errorMessage: string = '';
  // Mude para público para que o template possa acessá-lo
  public isBrowser: boolean; // Flag para saber se estamos no navegador

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {
    this.isBrowser = isPlatformBrowser(platformId);
    if (this.isBrowser) {
      this.loginForm = this.fb.group({
        email: ['', [Validators.required, Validators.email]],
        password: ['', [Validators.required]]
      });
    }
  }

  ngOnInit(): void {
    // Agora o ngOnInit só precisa lidar com redirecionamento, etc.
    if (this.isBrowser && this.authService.isAuthenticated()) {
      this.router.navigate(['/dashboard']);
    }
  }

  onSubmit(): void {
    this.errorMessage = '';

    // A validação agora é segura porque só é chamada se isBrowser for true
    if (!this.isBrowser || this.loginForm.invalid) {
      this.errorMessage = 'Por favor, preencha o email e a senha corretamente.';
      if (this.isBrowser && this.loginForm) { // Verifica se loginForm está definido antes de markAllAsTouched
        this.loginForm.markAllAsTouched();
      }
      return;
    }

    const { email, password } = this.loginForm.value;

    this.authService.login({ email, password }).subscribe({
      next: (res) => {
        console.log('Login successful:', res);
        this.router.navigate(['/material']);
      },
      error: (err) => {
        console.error('Login failed:', err);
        this.errorMessage = err.error?.message || 'Email ou senha inválidos.';
      }
    });
  }
}
