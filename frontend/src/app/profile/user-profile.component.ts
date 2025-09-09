import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { UserProfile, ProfileService } from './user-profile.service';
import { ReviewService } from '../transaction/services/review.service';
import { Review, UserAverage } from '../transaction/models/review.model';
import { environment } from '../../environments/environment';
import { AuthService } from '../auth/auth.service';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css'],
  imports: [CommonModule, FormsModule]
})
export class UserProfileComponent {
  apiUrl = environment.apiUrl;
  user?: UserProfile;  // Pode começar undefined
  originalUser?: UserProfile;
  reviews: Review[] = [];
  rating: UserAverage = {
    averageRating: 0,
    userId: 0,
    totalReviews: 0
  }; // Inicializa pra evitar undefined
  loading = true;
  isOwnProfile = false;
  editMode = false;
  selectedFile?: File;
  timestamp = new Date().getTime(); // para forçar reload da foto

  constructor(
    private profileService: ProfileService,
    private reviewService: ReviewService,
    private route: ActivatedRoute,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    const userId = Number(this.route.snapshot.paramMap.get('id'));
    const loggedUser = this.authService.getUserId();
    this.isOwnProfile = loggedUser === userId;

    this.profileService.getUserById(userId).subscribe({
      next: (user) => {
        this.user = {...user};
        this.originalUser = {...user};
        this.loading = false;
      },
      error: (err) => {
        console.error('Erro ao carregar usuário', err);
        this.loading = false;
      }
    });

    this.reviewService.getByUserId(userId).subscribe({
      next: (reviews) => (this.reviews = reviews),
      error: (err) => console.error('Erro ao carregar reviews', err)
    });

    this.reviewService.getUserRating(userId).subscribe({
      next: (rating) => (this.rating = rating),
      error: (err) => console.error('Erro ao carregar a média')
    });
  }

  onFileSelected(event: any) {
    this.selectedFile = event.target.files[0];
  }

  onSaveProfile(updated: { name: string; interests: string; file?: File }) {
    this.profileService.updateProfile(
      { name: updated.name, interests: updated.interests },
      updated.file
    ).subscribe({
      next: (user) => {
        this.user = user;
        this.originalUser = {...user};
        this.timestamp = new Date().getTime(); // força reload da foto
        this.editMode = false;
      },
      error: (err) => console.error("Erro ao atualizar perfil", err)
    });
  }

  onCancelEdit() {
    if (this.originalUser) {
      this.user = { ...this.originalUser }; // restaura os valores
    }
    this.editMode = false;
    this.selectedFile = undefined;
    this.timestamp = new Date().getTime();
  }

}
