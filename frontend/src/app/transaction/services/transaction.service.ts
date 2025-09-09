//src/app/transaction/services/transaction.service.ts
import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Transaction, TransactionDTO } from '../models/transaction.model';
import { Material } from '../../materials/models/material.model';

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

  getTransactionsByMaterial(idMaterial: number): Observable<TransactionDTO[]> {
    return this.http.get<TransactionDTO[]>(`${this.API_URL}/material/${idMaterial}`);
  }

  getById(idTransaction: number): Observable<TransactionDTO> {
    return this.http.get<TransactionDTO>(`${this.API_URL}/${idTransaction}`);
  }

  getTransactionsByUser(): Observable<TransactionDTO[]> {
    return this.http.get<TransactionDTO[]>(`${this.API_URL}/user`);
  }
}
