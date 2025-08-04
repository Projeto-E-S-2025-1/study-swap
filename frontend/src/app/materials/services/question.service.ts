import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Question } from '../models/question.model';

@Injectable({
  providedIn: 'root'
})
export class QuestionService {
  private readonly API_URL = 'http://localhost:8080/questions';
  private http = inject(HttpClient);

  getByMaterialId(materialId: number): Observable<Question[]> {
    return this.http.get<Question[]>(`${this.API_URL}/material/${materialId}`);
  }
}
