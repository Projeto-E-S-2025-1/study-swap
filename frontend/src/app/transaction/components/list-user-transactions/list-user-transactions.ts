//src/app/transaction/components/list-user-transactions/list-user-transactions.ts
import { Component, OnInit } from '@angular/core';
import { TransactionService } from '../../services/transaction.service';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-transactions',
  templateUrl: './list-user-transactions.html',
  styleUrls: ['./list-user-transactions.css'],
  imports: [RouterModule, CommonModule]
})
export class TransactionsComponent implements OnInit {
  transactions: any[] = [];

  constructor(private transactionService: TransactionService) {}

  ngOnInit(): void {
    this.loadTransactions();
  }

  loadTransactions() {
    this.transactionService.getTransactionsByUser().subscribe({
      next: (data) => {
        this.transactions = data;
      },
      error: (err) => {
        console.error('Erro ao carregar transações', err);
      }
    });
  }
}
