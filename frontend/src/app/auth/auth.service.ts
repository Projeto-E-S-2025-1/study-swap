// src/app/auth/auth.service.ts
import { Injectable, PLATFORM_ID, Inject, Injector } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { isPlatformBrowser } from '@angular/common';
import { JwtHelperService } from '@auth0/angular-jwt';
import { tap } from 'rxjs/operators';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private api = 'http://localhost:8080/auth';
  private jwtHelper: JwtHelperService;
  private isBrowser: boolean;

  constructor(
    private http: HttpClient,
    private router: Router,
    @Inject(PLATFORM_ID) private platformId: Object,
    private injector: Injector
  ) {
    this.isBrowser = isPlatformBrowser(platformId);

    if (this.isBrowser) {
      this.jwtHelper = new JwtHelperService();
    } else {
      this.jwtHelper = {
        isTokenExpired: () => true,
        tokenGetter: () => null,
        decodeToken: () => null
      } as unknown as JwtHelperService;
    }
  }

  login(credentials: { email: string; password: string }) {
    return this.http.post<{ token: string }>(`${this.api}/login`, credentials)
      .pipe(
        tap((response: { token: string }) => {
          if (this.isBrowser && response && response.token) {
            localStorage.setItem('token', response.token);
          }
        })
      );
  }

  register(data: any) {
    console.log('PASSO 3.1: AuthService recebendo dados para registro:', data);
    return this.http.post<any>(`${this.api}/register`, data);
  }

  logout() {
    if (this.isBrowser) { // Use isBrowser
      localStorage.removeItem('token');
    }
    this.router.navigate(['/login']);
  }

  isAuthenticated(): boolean {
    if (!this.isBrowser) {
      return false;
    }
    const token = localStorage.getItem('token');
    return token != null && !this.jwtHelper.isTokenExpired(token);
  }

  getToken(): string | null {
    if (!this.isBrowser) {
      return null;
    }
    return localStorage.getItem('token');
  }

  decodeToken(token: string): any {
    if (!this.isBrowser || !token) {
      return null;
    }
    try {
      return this.jwtHelper.decodeToken(token);
    } catch (error) {
      console.error('Erro ao decodificar o token:', error);
      return null;
    }
  }

  getUserId(): number | null {
    if (!this.isBrowser) {
      return null;
    }

    const token = this.getToken();
    if (!token || this.jwtHelper.isTokenExpired(token)) {
      return null;
    }

    try {
      const decodedToken = this.jwtHelper.decodeToken(token);
      return decodedToken?.userId || null;
    } catch (error) {
      console.error('Erro ao extrair ID do usuário do token:', error);
      return null;
    }
  }
}
