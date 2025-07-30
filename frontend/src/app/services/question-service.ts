import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { catchError } from 'rxjs';

@Injectable({
  providedIn: 'root'
})

export class QuestionService {
  private apiUrl = `http://localhost:8080`;
  private http = inject(HttpClient);
  router = inject(Router);
  
  postQuestion(descricao: string, titulo:string): void {
    this.http.post<any>(`${this.apiUrl}/question`, {descricao, titulo})
        .pipe(
            catchError((error) => {
              console.error(error);
              throw error;
            })
        )
        .subscribe((response) => {
            console.log('Token: ', response.token);
            localStorage.setItem('token', response.token);
        })
  }

  deleteQuestion(id:number): void {
    this.http.delete<any>(`${this.apiUrl}/question/${id}`)
        .pipe(
          catchError((error) => {
            console.error(error);
            throw error;
          })
        )
        .subscribe((response) => {
          console.log('Token: ', response.token);
        })
  }
}
