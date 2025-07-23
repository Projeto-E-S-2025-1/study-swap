// src/app/auth/register/register.component.ts
import { Component, OnInit, Inject, PLATFORM_ID } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router'; // Adicione RouterLink
import { AuthService } from '../auth.service';
import { Role } from '../../models/role.enum';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {
  registerForm!: FormGroup; // Declare, mas não inicialize aqui
  roles = Object.values(Role);
  errorMessage: string = '';
  public isBrowser: boolean; // Mude para público

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {
    this.isBrowser = isPlatformBrowser(platformId); // Defina a flag no construtor

    // Inicialize o formulário AQUI no CONSTRUTOR, mas apenas se estiver no navegador.
    if (this.isBrowser) {
      this.registerForm = this.fb.group({
        name: ['', [Validators.required, Validators.minLength(3)]],
        email: ['', [Validators.required, Validators.email]],
        password: ['', [Validators.required, Validators.minLength(6)]],
        role: [Role.USER, Validators.required]
      });
    }
  }

  ngOnInit(): void {
    // Não precisa de lógica de formulário aqui, apenas lógica que depende de dados
  }

  onSubmit(): void {
    this.errorMessage = '';

    if (!this.isBrowser) {
      console.warn('Attempted form submission during server-side rendering. Skipping.');
      return;
    }
     console.log('PASSO 1: Formulário submetido. Validação:', this.registerForm.valid, 'Touched:', this.registerForm.touched, 'Dirty:', this.registerForm.dirty);
    if (this.registerForm.invalid) {
      this.errorMessage = 'Por favor, preencha todos os campos corretamente.';
      if (this.isBrowser && this.registerForm) {
        this.registerForm.markAllAsTouched();
         console.log('PASSO 2: Erros de validação do formulário:', this.registerForm.errors, this.registerForm.controls['name'].errors, this.registerForm.controls['email'].errors, this.registerForm.controls['password'].errors, this.registerForm.controls['role'].errors);
      }
      return;
    }

    const { name, email, password, role } = this.registerForm.value;
    console.log('Dados enviados para o servidor (cadastro):', { name, email, password, role });

    this.authService.register({ name, email, password, role }).subscribe({
      next: (res) => {
        console.log('User registered successfully');
        console.log('Resposta do servidor (cadastro):', res);
        this.router.navigate(['/login']);
      },
      error: (err) => {
        console.error('Registration failed:', err);
        this.errorMessage = err.error?.message || 'Erro ao registrar. Email já cadastrado ou dados inválidos.';
      }
    });
  }
}
