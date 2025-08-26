import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Transaction } from '../models/transaction.model';
import { Material } from '../../materials/models/material.model';
import { stringify } from 'querystring';

@Injectable({
  providedIn: 'root'
})
export class TransactionService {
  private readonly API_URL = `${environment.apiUrl}/transactions`;
  private http = inject(HttpClient);

  createTransaction(idMaterial: number, materialTrade: Partial<Material>): Observable<Transaction> {
    return this.http.post<Transaction>(`${this.API_URL}/create/${idMaterial}`, materialTrade);
  }

  cancelTransaction(idMaterial: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${idMaterial}`);
  }

  completeTransaction(idTransaction: number): Observable<Transaction> {
    return this.http.put<Transaction>(`${this.API_URL}/${idTransaction}`, {});
  }

  getTransactionsByMaterial(idMaterial: number): Observable<Transaction[]> {
    return this.http.get<Transaction[]>(`${this.API_URL}/material/${idMaterial}`);
  }

  getById(idTransaction: number): Observable<Transaction> {
    return this.http.get<Transaction>(`${this.API_URL}/${idTransaction}`);
  }

  getTransactionsByUser(): Observable<Transaction[]> {
    return this.http.get<Transaction[]>(`${this.API_URL}/user`);
  }
}
