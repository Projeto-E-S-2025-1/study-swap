// src/app/app.ts
import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HeaderComponent } from './shared/header/header.component'; // Importe seu componente de header

@Component({
  selector: 'app-root',
  standalone: true, // Garanta que é standalone
  imports: [RouterOutlet, HeaderComponent], // Adicione HeaderComponent aqui
  template: `
    <app-header></app-header>
    <main>
      <router-outlet></router-outlet>
    </main>
  `,
  styleUrls: ['./app.component.scss'] // Crie este arquivo CSS se não existir
})
export class App {

}
