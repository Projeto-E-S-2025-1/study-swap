import { Component, inject } from '@angular/core';
import { AuthService } from '../../services/auth-service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-login',
  imports: [FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class Login {
  authService = inject(AuthService);
  email = '';
  senha = '';

  login(): void {
    console.log(this.email, this.senha);
    this.authService.login(this.email, this.senha);
  }
}
