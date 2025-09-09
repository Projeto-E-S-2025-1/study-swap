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

    updateProfile(userDTO: any, file?: File) {
    const formData = new FormData();
    formData.append("userDTO", new Blob([JSON.stringify(userDTO)], { type: "application/json" }));
    if (file) {
        formData.append("file", file);
    }

    return this.http.put<UserProfile>(`${this.API_URL}/me`, formData);
    }
}

export interface UserProfile {
    id: number;
    name: string;
    photoUrl: string;
    interests: string;
    role: Role;
}