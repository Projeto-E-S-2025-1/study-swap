// src/app/shared/header/header.component.ts
import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router'; // Remova RouterOutlet se ainda estiver aqui
import { AuthService } from '../../auth/auth.service';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators'; // Adicione map se estiver usando no isAuthenticated$

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule, RouterLink], // Certifique-se que RouterOutlet foi removido
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent {
  isAuthenticated$: Observable<boolean>;

  constructor(private authService: AuthService, private router: Router) {
    // Você pode simplificar esta linha para usar o getter diretamente, ou se aprofundar em Observables
    // Para SSR, o BehaviorSubject ou Observable dentro do AuthService precisaria ser tratado com `isPlatformBrowser` também.
    // O getter `isUserAuthenticated` no HTML é uma abordagem mais simples aqui.
    this.isAuthenticated$ = new Observable(observer => {
        // Para SSR, o estado inicial será false, mas será atualizado no browser
        observer.next(this.authService.isAuthenticated());
    });
    // Ou simplesmente:
    // this.isAuthenticated$ = this.authService.isAuthenticated$(); // Se AuthService tivesse um Observable
  }

  logout() {
    this.authService.logout();
  }

  // Este getter chamará a versão SSR-safe do isAuthenticated()
  get isUserAuthenticated(): boolean {
    return this.authService.isAuthenticated();
  }
}