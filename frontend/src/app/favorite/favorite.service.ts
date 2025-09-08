import { inject, Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Router } from 'express';
import { Observable } from 'rxjs';
import { Material } from '../materials/models/material.model';

@Injectable({
  providedIn: 'root'
})

export class FavoriteService {
  private readonly API_URL = `${environment.apiUrl}/user/favorites`;
  private http = inject(HttpClient);
  router = inject(Router);

  getFavorites(): Observable<Material[]> {
    return this.http.get<Material[]>(this.API_URL);
  }

  favoriteMaterial(idMaterial: number): void {
    this.http.put<Material[]>(`${this.API_URL}/${idMaterial}`, null);
  }
  
  unfavoriteMaterial(idMaterial: number): void {
    this.http.delete<Material[]>(`${this.API_URL}/${idMaterial}`);
  }
}
