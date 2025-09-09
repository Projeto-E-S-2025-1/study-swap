import { Injectable, inject } from "@angular/core";
import { environment } from "../../environments/environment";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { Router } from '@angular/router';
import { Role } from "../models/role.enum";

@Injectable({
    providedIn: 'root'
})
export class ProfileService {

    private readonly API_URL = `${environment.apiUrl}/user`;
    private http = inject(HttpClient);
    router = inject(Router);

    getUserById(id: number): Observable<UserProfile>{
        return this.http.get<UserProfile>(`${this.API_URL}/${id}`)
    }
}

export interface UserProfile {
    id: number;
    name: string;
    photo_url: string;
    interests: string;
    role: Role;
}