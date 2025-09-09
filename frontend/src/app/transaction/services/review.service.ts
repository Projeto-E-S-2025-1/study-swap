//src/app/transaction/services/review.service.ts
import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Review, UserAverage } from '../models/review.model';
import { AuthService } from '../../auth/auth.service';
import { environment } from '../../../environments/environment';

export interface ReviewRequestDTO {
    authorId: number;
    description: string;
    rating: number;
    transactionId: number;
}

@Injectable({
    providedIn: 'root'
})
export class ReviewService {
    private readonly API_URL = `${environment.apiUrl}`;
    private http = inject(HttpClient);
    private authService = inject(AuthService);

    create(transactionId: number, reviewDTO: ReviewRequestDTO): Observable<Review> {
        const headers = this.getAuthHeaders();
        return this.http.post<Review>(`${this.API_URL}/review/${transactionId}`, reviewDTO, { headers });
    }

    getByTransactionId(transactionId: number): Observable<Review> {
        return this.http.get<Review>(`${this.API_URL}/review/${transactionId}`);
    }

    getByUserId(userId: number): Observable<Review[]> {
        return this.http.get<Review[]>(`${this.API_URL}/review/user/${userId}`);
    }

    getUserRating(userId: number): Observable<UserAverage>{
        return this.http.get<UserAverage>(`${this.API_URL}/review/user/${userId}/average`);
    }

    private getAuthHeaders(): HttpHeaders {
        const token = this.authService.getToken();
        return new HttpHeaders({
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        });
    }
}