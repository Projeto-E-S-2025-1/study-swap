import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Question } from '../models/question.model';
import { AuthService } from '../../auth/auth.service';

export interface CreateQuestionDTO {
  title: string;
  description: string;
  materialId: number;
}

export interface UpdateQuestionDTO {
  title?: string;
  description?: string;
}

@Injectable({
  providedIn: 'root'
})
export class QuestionService {
  private readonly API_URL = 'http://localhost:8080/questions';
  private http = inject(HttpClient);
  private authService = inject(AuthService);

  getByMaterialId(materialId: number): Observable<Question[]> {
    return this.http.get<Question[]>(`${this.API_URL}/material/${materialId}`);
  }

  create(questionDTO: CreateQuestionDTO): Observable<Question> {
    const headers = this.getAuthHeaders();
    return this.http.post<Question>(this.API_URL, questionDTO, { headers });
  }

  update(questionId: number, questionDTO: UpdateQuestionDTO): Observable<Question> {
    const headers = this.getAuthHeaders();
    return this.http.put<Question>(`${this.API_URL}/${questionId}`, questionDTO, { headers });
  }

  getById(questionId: number): Observable<Question> {
    return this.http.get<Question>(`${this.API_URL}/${questionId}`);
  }

  delete(questionId: number): Observable<void> {
    const headers = this.getAuthHeaders();
    return this.http.delete<void>(`${this.API_URL}/${questionId}`, { headers });
  }

  private getAuthHeaders(): HttpHeaders {
    const token = this.authService.getToken();
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
  }
}
