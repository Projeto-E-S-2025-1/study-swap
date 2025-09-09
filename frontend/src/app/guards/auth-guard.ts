//src/app/guards/auth-guard.ts
import { inject } from '@angular/core';
import { CanActivateFn } from '@angular/router';
import { routes } from '../app.routes';
// ...existing code...
import { Router } from '@angular/router';
// ...existing code...
const router = inject(Router);

export const authGuard: CanActivateFn = () => {
  if(localStorage.getItem('token')) return true;
  router.navigate(['/login']);
  return false;
};
