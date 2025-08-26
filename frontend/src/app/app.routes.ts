// src/app/app.routes.ts
import { Routes } from '@angular/router';
import { LoginComponent } from './auth/login/login.component';
import { RegisterComponent } from './auth/register/register.component';
import { AuthGuard } from './auth/auth.guard';
import { MATERIALS_ROUTES } from './materials/material.routes';
import { TRANSACTION_ROUTES } from './transaction/transaction.routes';
import { ReportFormComponent } from './reports/report-form/report-form';

// Importações corretas para os componentes
import { Home } from './home/home';

export const routes: Routes = [
  { path: '', component: Home, title: 'Home - StudySwap' },
  { path: 'login', component: LoginComponent, title: 'Login - StudySwap' },
  { path: 'register', component: RegisterComponent, title: 'Cadastro - StudySwap' },
  { path: 'material', canActivate: [AuthGuard], children: MATERIALS_ROUTES},
  { path: 'transaction', canActivate: [AuthGuard], children: TRANSACTION_ROUTES},
  { path: 'report', canActivate: [AuthGuard], component: ReportFormComponent, title: 'Denunciar - StudySwap' },
  { path: '**', redirectTo: '', pathMatch: 'full' }
];
