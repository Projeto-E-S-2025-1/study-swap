//src/app/shared/components/navbar.component.ts

// Update the import path if the service is actually located elsewhere, for example:
import { AuthService } from '../../auth/auth.service'; // Adjust the path as necessary
// Or use the correct relative path based on your project structure.

export class NavbarComponent {
  constructor(private authService: AuthService) {}

  logout() {
    this.authService.logout();
  }
}
