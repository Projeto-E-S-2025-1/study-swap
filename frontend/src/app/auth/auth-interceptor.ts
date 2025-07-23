// src/app/auth/auth.interceptor.ts
import { HttpRequest, HttpHandlerFn, HttpEvent, HttpInterceptorFn } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';
import { inject } from '@angular/core'; // Para injetar o serviço em uma função

// Exporte uma função de interceptor
export const authInterceptor: HttpInterceptorFn = (
  request: HttpRequest<unknown>,
  next: HttpHandlerFn 
): Observable<HttpEvent<unknown>> => {

  const authService = inject(AuthService); // Injete o serviço usando inject()

  const token = authService.getToken();

  // Verifique se a rota é de login ou registro para NÃO adicionar o token
  const isAuthRequest = request.url.includes('/auth/login') || request.url.includes('/auth/register');

  if (token && !isAuthRequest) {
    request = request.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
  }

  return next(request); // Chame next(request) para continuar a cadeia de interceptores
};