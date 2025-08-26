import { Component } from '@angular/core';
import { CommonModule } from '@angular/common'
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-review-form',
  templateUrl: './review-transaction.component.html',
  styleUrls: ['./review-transaction.component.css'],
  imports: [CommonModule, FormsModule]
})
export class ReviewFormComponent {
  reviewDTO = {
    rating: 0,
    comment: ''
  };

  stars = [1, 2, 3, 4, 5];
  hoveredRating = 0;

  successMessage: string | null = null;
  errorMessage: string | null = null;

  setRating(rating: number) {
    this.reviewDTO.rating = rating;
  }

  hoverRating(rating: number) {
    this.hoveredRating = rating;
  }

  onSubmit() {
    if (this.reviewDTO.rating > 0 && this.reviewDTO.comment.trim()) {
      this.successMessage = "Avaliação enviada com sucesso!";
      this.errorMessage = null;
      // Aqui poderia chamar um service para salvar no backend
      this.reviewDTO = { rating: 0, comment: '' };
    } else {
      this.errorMessage = "Preencha todos os campos antes de enviar.";
      this.successMessage = null;
    }
  }
}
