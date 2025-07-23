// src/app/app.routes.ts
import { Routes } from '@angular/router';
import { LoginComponent } from './auth/login/login.component';
import { RegisterComponent } from './auth/register/register.component';
import { AuthGuard } from './auth/auth.guard';

// Importações corretas para os componentes
import { Dashboard } from './dashboard/dashboard';
import { Home } from './home/home';

export const routes: Routes = [
  { path: '', component: Home, title: 'Home - StudySwap' },
  { path: 'login', component: LoginComponent, title: 'Login - StudySwap' },
  { path: 'register', component: RegisterComponent, title: 'Cadastro - StudySwap' },
  {
    path: 'dashboard',
    component: Dashboard,
    canActivate: [AuthGuard],
    title: 'Dashboard - StudySwap'
  },
  { path: '**', redirectTo: '', pathMatch: 'full' }
];
