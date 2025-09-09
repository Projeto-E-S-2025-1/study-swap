//src/app/transaction/components/review-transaction/review-transaction.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ReviewService } from '../../services/review.service';
import { AuthService } from '../../../auth/auth.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-review-form',
  templateUrl: './review-transaction.component.html',
  styleUrls: ['./review-transaction.component.css'],
  imports: [CommonModule, FormsModule]
})
export class ReviewFormComponent implements OnInit {
  transactionId!: number;

  reviewDTO = {
    authorId: 0,
    rating: 0,
    description: '',
    transactionId: 0
  };

  stars = [1, 2, 3, 4, 5];
  hoveredRating = 0;

  successMessage: string | null = null;
  errorMessage: string | null = null;
  userId: number | null = null;

  constructor(
    private reviewService: ReviewService,
    private authService: AuthService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit() {
    this.transactionId = Number(this.route.snapshot.paramMap.get('id'));
    this.userId = this.authService.getUserId();
    if (this.userId == null) {
    throw new Error("Usuário não identificado");
    }
    this.reviewDTO.authorId = this.userId;
    this.reviewDTO.transactionId = this.transactionId;
  }

  setRating(rating: number) {
    this.reviewDTO.rating = rating;
  }

  hoverRating(rating: number) {
    this.hoveredRating = rating;
  }

  onSubmit() {
    if (!this.userId) {
      this.errorMessage = "Usuário não identificado";
      return;
    }

    if (this.reviewDTO.rating > 0 && this.reviewDTO.description.trim()) {
      this.reviewService.create(this.transactionId, this.reviewDTO).subscribe({
        next: () => {
          this.successMessage = "Avaliação enviada com sucesso!";
          this.errorMessage = null;
          this.reviewDTO.rating = 0;
          this.reviewDTO.description = '';
          this.hoveredRating = 0;
          this.router.navigate(['/material/'])
        },
        error: () => {
          this.errorMessage = "Erro ao enviar avaliação.";
          this.successMessage = null;
        }
      });
    } else {
      this.errorMessage = "Preencha todos os campos antes de enviar.";
      this.successMessage = null;
    }
  }
}
