// src/app/home/home.ts
import { Component } from '@angular/core';
import { CommonModule } from '@angular/common'; // Importe CommonModule para diretivas comuns
import { RouterLink } from '@angular/router';   // Importe RouterLink se for usar links de rota

@Component({
  selector: 'app-home',
  standalone: true, 
  imports: [
    CommonModule, // Necessário para *ngIf, *ngFor, etc.
    RouterLink    // Necessário se houver <a routerLink="...">
  ],
  templateUrl: './home.html', 
  styleUrl: './home.css'      
})
export class Home { // Nome da classe agora é Home
  // Seu código do componente aqui
}