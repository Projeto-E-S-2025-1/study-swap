import { Component, Input, Output, EventEmitter } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ConservationStatus, Material, MaterialType, TransactionType } from '../../../materials/models/material.model';
import { TransactionService } from '../../services/transaction.service';
import { AuthService } from '../../../auth/auth.service';

@Component({
  selector: 'app-open-transaction',
  standalone: true,
  templateUrl: './open-transaction.component.html',
  styleUrls: ['./open-transaction.component.css'],
  imports: [CommonModule, FormsModule],
})
export class OpenTransactionComponent {
  @Input() material!: Material;
  @Input() transactionType!: 'DOACAO' | 'VENDA' | 'TROCA';
  @Input() isDone!: boolean;

  @Output() close = new EventEmitter<void>();
  @Output() confirm = new EventEmitter<void>();
  @Output() cancel = new EventEmitter<void>();

  isLoading = false;
  errorMessage = '';
  MaterialType = MaterialType;
  ConservationStatus = ConservationStatus;
  constructor(
    private transactionService: TransactionService,
    private authService: AuthService) {}

  materialDTO = {
    title: 'titulo',
    description: '',
    materialType: MaterialType.LIVRO,
    conservationStatus: ConservationStatus.BOM,
    transactionType: TransactionType.TROCA,
    price: 0 as number | null,
    userId: 0 as number,
    available: false
  };

  onClose(){
    this.close.emit()
  }
  
  onCancel() {
    if(!this.material.id) {
      this.errorMessage = 'Material inválido';
      return;
    }
    this.transactionService.cancelTransaction(this.material.id).subscribe({
      next: (res) => {
        console.log('Transação cancelada:', res);
        this.isLoading = false;
        this.cancel.emit();
        this.close.emit();
      },
      error: (err) => {
        this.errorMessage = 'Erro ao cancelar transação';
        this.isLoading = false;
        console.error('Erro ao cancelar transação:', err);
        this.close.emit();
      }
    });
  }

  onConfirm() {
    this.isLoading = true;
    this.errorMessage = '';

    if(!this.material.id) {
      this.errorMessage = 'Material inválido';
      this.isLoading = false;
      return;
    }

    const materialToSend = { ...this.materialDTO };
    if (materialToSend.transactionType !== TransactionType.VENDA) {
      materialToSend.price = null;
    }
    materialToSend.userId = Number(this.authService.getUserId());
    this.transactionService.createTransaction(this.material.id, materialToSend).subscribe({
      next: (res) => {
        console.log('Transação criada (troca):', res);
        this.isLoading = false;
        this.confirm.emit();
        this.close.emit();
      },
      error: (err) => {
        this.errorMessage = 'Erro ao criar transação';
        this.isLoading = false;
        console.error('Erro ao criar transação (troca):', err);
        this.close.emit();
      }
    });
  }
}
