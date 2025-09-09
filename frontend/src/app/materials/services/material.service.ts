//src/app/materials/services/material.service.ts
import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';
import { Material } from '../models/material.model';
import { environment } from '../../../environments/environment';
import { MaterialDTO } from '../../transaction/models/transaction.model';

@Injectable({
  providedIn: 'root'
})
export class MaterialService {
  private readonly API_URL = `${environment.apiUrl}/material`;
  private http = inject(HttpClient);
  router = inject(Router);

  getAll(): Observable<Material[]> {
    return this.http.get<Material[]>(this.API_URL);
  }

  getById(id: number): Observable<Material> {
    return this.http.get<Material>(`${this.API_URL}/${id}`);
  }

  getByFilter(filters: string): Observable<Material[]> {
    return this.http.get<Material[]>(`${this.API_URL}/search?${filters}`);
  }

  getAvailableMaterials(): Observable<Material[]> {
    return this.http.get<Material[]>(`${this.API_URL}/available`);
  }

  getByUser(): Observable<Material[]> {
    return this.http.get<Material[]>(`${this.API_URL}/user`);
  }

  create(material: MaterialDTO, file?: File): Observable<Material> {
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

  update(id: number, material: Partial<Material>, file?: File): Observable<Material> {
    const formData = new FormData();

    // Transforma o DTO em string JSON
    const jsonBlob = new Blob([JSON.stringify(material)], { type: 'application/json' });
    formData.append('materialDTO', jsonBlob);

    // Anexa o arquivo (se houver)
    if (file) {
      formData.append('file', file);
    }

    return this.http.put<Material>(`${this.API_URL}/${id}`, formData);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${id}`);
  }
}
