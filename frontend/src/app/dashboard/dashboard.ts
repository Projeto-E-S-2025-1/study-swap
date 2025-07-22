// src/app/dashboard/dashboard.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from '../auth/auth.service'; 

@Component({
  selector: 'app-dashboard',
  standalone: true, 
  imports: [
    CommonModule // Necessário para *ngIf, *ngFor, etc.
  ],
  templateUrl: './dashboard.html', 
  styleUrl: './dashboard.css'       
})
export class Dashboard implements OnInit { 
  userEmail: string | null = null; // Propriedade para armazenar o e-mail do usuário

  constructor(private authService: AuthService) { } // Injete o AuthService

  ngOnInit(): void {

    const token = this.authService.getToken();
    if (token) {

      this.userEmail = 'exemplo@email.com'; // Substitua pela lógica real de decodificação do token
    }
  }
}