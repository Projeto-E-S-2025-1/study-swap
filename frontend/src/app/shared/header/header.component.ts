// src/app/shared/header/header.component.ts
import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import {  RouterLink } from '@angular/router'; 
import { AuthService } from '../../auth/auth.service';


@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule, RouterLink], 
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent {
    constructor(private authService: AuthService) {
    // A abordagem com getter é mais direta e SSR-friendly aqui
  }

  logout() {
    this.authService.logout();
  }

  // Este getter chamará a versão SSR-safe do isAuthenticated()
  get isUserAuthenticated(): boolean {
    return this.authService.isAuthenticated();
  }
}