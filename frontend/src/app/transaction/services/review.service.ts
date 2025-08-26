import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Review } from '../models/review.model';
import { AuthService } from '../../auth/auth.service';

export interface CreateReviewDTO {
    description: string;
    rating: number;
    transactionId: number;
}

@Injectable({
    providedIn: 'root'
})
export class ReviewService {
    private readonly API_URL = 'http://localhost:8080/transactions';
    private http = inject(HttpClient);
    private authService = inject(AuthService);

    create(transactionId: number, reviewDTO: CreateReviewDTO): Observable<Review> {
        const headers = this.getAuthHeaders();
        return this.http.post<Review>(`${this.API_URL}/${transactionId}/reviews`, reviewDTO, { headers });
    }

    getByTransactionId(transactionId: number): Observable<Review> {
        return this.http.get<Review>(`${this.API_URL}/${transactionId}/reviews`);
    }

    private getAuthHeaders(): HttpHeaders {
        const token = this.authService.getToken();
        return new HttpHeaders({
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        });
    }
}