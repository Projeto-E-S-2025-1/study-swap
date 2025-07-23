// src/app/dashboard/dashboard.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from '../auth/auth.service'; 

@Component({
  selector: 'app-dashboard',
  standalone: true, 
  imports: [
    CommonModule 
  ],
  templateUrl: './dashboard.html', 
  styleUrl: './dashboard.css'       
})
export class Dashboard implements OnInit {
  userEmail: string | null = null;
  userName: string | null = null; 

  constructor(private authService: AuthService) { }

  ngOnInit(): void {
    const token = this.authService.getToken();
    if (token) {
      const decodedToken = this.authService.decodeToken(token);
      if (decodedToken) {
        this.userEmail = decodedToken.email || null; // Assumindo que o token tem 'email'
        this.userName = decodedToken.name || null; // Assumindo que o token tem 'name'
      }
    }
  }
}