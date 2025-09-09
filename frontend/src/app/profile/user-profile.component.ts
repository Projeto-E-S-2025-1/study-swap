//src/app/profile/user-profile.component.ts
import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserProfile, ProfileService } from './user-profile.service';
import { ActivatedRoute } from '@angular/router';
import { ReviewService } from '../transaction/services/review.service';
import { Review, UserAverage } from '../transaction/models/review.model';
import { environment } from '../../environments/environment';

@Component({
  selector: 'app-user-profile',
  templateUrl: 'user-profile.component.html',
  styleUrls: ['user-profile.component.css'],
  imports: [CommonModule]
})
export class UserProfileComponent {
  apiUrl = environment.apiUrl;
  user!: UserProfile;
  reviews!: Review[];
  rating!: UserAverage;
  loading = true;

  constructor(
    private profileService: ProfileService,
    private reviewService: ReviewService, 
    private route: ActivatedRoute) {}

  ngOnInit(): void {
    const userId = Number(this.route.snapshot.paramMap.get('id'));

    this.profileService.getUserById(userId).subscribe({
      next: (user) => {
        this.user = user;
        this.loading = false;
      },
      error: (err) => {
        console.error('Erro ao carregar usuário', err);
        this.loading = false;
      }
    });

    this.reviewService.getByUserId(userId).subscribe({
      next: (reviews) => {
        this.reviews = reviews;
      },
      error: (err) => {
        console.error('Erro ao carregar reviews', err);
      }
    });

    this.reviewService.getUserRating(userId).subscribe({
      next: (rating) => {
        this.rating = rating;
      },
      error: (err) => {
        console.error('Erro ao carregar a média')
      }
    })
  }
}