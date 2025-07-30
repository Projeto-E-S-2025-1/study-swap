import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';
import { Material } from '../models/material.model';

@Injectable({
  providedIn: 'root'
})
export class MaterialService {
  private readonly API_URL = 'http://localhost:8080/material';
  private http = inject(HttpClient);
  router = inject(Router);

  getAll(): Observable<Material[]> {
    return this.http.get<Material[]>(this.API_URL);
  }

  getById(id: number): Observable<Material> {
    return this.http.get<Material>(`${this.API_URL}/${id}`);
  }

  create(material: Partial<Material>, file?: File): Observable<Material> {
    const formData = new FormData();

    // Transforma o DTO em string JSON
    const jsonBlob = new Blob([JSON.stringify(material)], { type: 'application/json' });
    formData.append('materialDTO', jsonBlob);

    // Anexa o arquivo (se houver)
    if (file) {
      formData.append('file', file);
    }

    return this.http.post<Material>(this.API_URL, formData);
  }

  update(id: number, material: Partial<Material>): Observable<Material> {
    return this.http.put<Material>(`${this.API_URL}/${id}`, material);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${id}`);
  }
}
