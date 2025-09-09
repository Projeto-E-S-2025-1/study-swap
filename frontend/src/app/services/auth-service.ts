//src/app/services/auth-service.ts
import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { catchError } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
//
export class AuthService {
  private apiUrl = environment.apiUrl;
  private http = inject(HttpClient);
  router = inject(Router);

  login(email: string, password: string): void {
    this.http.post<any>(`${this.apiUrl}/login`, {email, password})
      .pipe(
        catchError((error) => {
          console.error(error);
          throw error;
        })
      )
      .subscribe((response) => {
        console.log('Token: ', response.token);
        localStorage.setItem('token', response.token);
        this.router.navigate(['\home']);
      }
    );
  }

  logout(): void {
    localStorage.removeItem('token');
  }
}
