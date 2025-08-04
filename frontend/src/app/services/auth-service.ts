import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { catchError } from 'rxjs';


@Injectable({
  providedIn: 'root'
})
//
export class AuthService {
  private apiUrl = `http://localhost:8080`;
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
