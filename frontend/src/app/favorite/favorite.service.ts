import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';
import { Material } from '../materials/models/material.model';
import { environment } from '../../environments/environment';

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

  favoriteMaterial(idMaterial: number): Observable<void> {
    return this.http.put<void>(`${this.API_URL}/${idMaterial}`, null);
  }
  
  unfavoriteMaterial(idMaterial: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${idMaterial}`);
  }
}
