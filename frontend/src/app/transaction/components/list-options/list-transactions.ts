//src/app/transaction/components/list-options/list-transactions.ts
import { Component, Input, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Transaction } from '../../models/transaction.model';
import { TransactionService } from '../../services/transaction.service';

@Component({
  selector: 'app-transaction-proposals',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './list-transactions.html',
  styleUrls: ['./list-transactions.css']
})
export class TransactionProposalsComponent implements OnInit {
  @Input() materialId!: number;

  proposals: Transaction[] = [];
  selectedProposal: Transaction | null = null;
  errorMessage: string = '';
  isLoading: boolean = true;
  confirmedSelection: boolean = false;

  constructor(private transactionService: TransactionService) {}

  ngOnInit(): void {
    this.loadProposals();
  }

  loadProposals(): void {
    this.transactionService.getTransactionsByMaterial(this.materialId).subscribe({
      next: (data) => {
        this.proposals = data;
        this.isLoading = false;
      },
      error: () => {
        this.errorMessage = 'Erro ao carregar propostas.';
        this.isLoading = false;
      }
    });
  }

  selectProposal(proposal: Transaction): void {
    this.selectedProposal = proposal;
  }

  confirmSelection(): void {
  this.confirmedSelection = true;
}

  finalizeTransaction(): void {
    if (!this.selectedProposal) return;

    this.transactionService.completeTransaction(this.selectedProposal.id).subscribe({
      next: () => {
        alert('Transação finalizada com sucesso!');
        this.loadProposals();
        this.selectedProposal = null;
      },
      error: () => {
        alert('Erro ao finalizar transação.');
      }
    });
  }
}
