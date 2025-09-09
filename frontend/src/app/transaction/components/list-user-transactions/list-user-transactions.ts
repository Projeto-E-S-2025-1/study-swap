//src/app/transaction/components/list-user-transactions/list-user-transactions.ts
import { Component, OnInit } from '@angular/core';
import { TransactionService } from '../../services/transaction.service';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { Transaction, TransactionDTO } from '../../models/transaction.model';
import { ReviewService } from '../../services/review.service';
import { AuthService } from '../../../auth/auth.service';

@Component({
  selector: 'app-transactions',
  templateUrl: './list-user-transactions.html',
  styleUrls: ['./list-user-transactions.css'],
  imports: [RouterModule, CommonModule]
})
export class TransactionsComponent implements OnInit {
  transactions: TransactionDTO[] = [];
  hasReviewByTransaction: { [key: number]: boolean } = {};
  currentUserId!: number;

  constructor(
    private transactionService: TransactionService,
    private reviewService: ReviewService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    const userId = this.authService.getUserId();
    if(userId == null){
      throw new Error("Usuário não identificado");
    }
    this.currentUserId = userId;
    this.loadTransactions();
  }

  loadTransactions() {
    this.transactionService.getTransactionsByUser().subscribe({
      next: (data) => {
        this.transactions = data;

        this.transactions.forEach(tx => {
          // checa se existe review
          this.reviewService.getByTransactionId(tx.id).subscribe({
            next: (review) => {
              this.hasReviewByTransaction[tx.id] = !!review;
            },
            error: () => {
              this.hasReviewByTransaction[tx.id] = false;
            }
          });
        });
      },
      error: (err) => {
        console.error('Erro ao carregar transações', err);
      }
    });
  }

  hasReview(transactionId: number): boolean {
    return this.hasReviewByTransaction[transactionId] || false;
  }

  isReceiver(transaction: Transaction): boolean {
    return transaction.receiverId === this.currentUserId;
  }
}
