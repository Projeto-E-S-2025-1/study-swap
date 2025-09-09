//src/app/transaction/components/open-transaction/open-transaction.component.ts
import { Component, Input, Output, EventEmitter } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ConservationStatus, Material, MaterialType, TransactionType } from '../../../materials/models/material.model';
import { TransactionService } from '../../services/transaction.service';
import { AuthService } from '../../../auth/auth.service';
import { MaterialDTO } from '../../models/transaction.model';

@Component({
  selector: 'app-open-transaction',
  standalone: true,
  templateUrl: './open-transaction.component.html',
  styleUrls: ['./open-transaction.component.css'],
  imports: [CommonModule, FormsModule],
})
export class OpenTransactionComponent {
  @Input() material!: Material;
  @Input() transactionType!: TransactionType;
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

    let materialToSend: MaterialDTO | null = null;

    switch (this.transactionType) {
      case TransactionType.TROCA:
        materialToSend = {
          title: this.materialDTO.title,
          description: this.materialDTO.description,
          materialType: this.materialDTO.materialType,
          conservationStatus: this.materialDTO.conservationStatus,
          transactionType: TransactionType.TROCA,
        };
        break;
      
      case TransactionType.VENDA:
        if (!this.materialDTO.price || this.materialDTO.price <= 0) {
          this.errorMessage = 'Preço inválido';
          this.isLoading = false;
          return;
        }
        materialToSend = null;
        break;

      case TransactionType.DOACAO:
        materialToSend = null;
        break;
    }

    this.transactionService.createTransaction(this.material.id, materialToSend).subscribe({
      next: (res) => {
        console.log('Transação criada:', res);
        this.isLoading = false;
        this.confirm.emit();
        this.close.emit();
      },
      error: (err) => {
        this.errorMessage = 'Erro ao criar transação';
        this.isLoading = false;
        console.error('Erro ao criar transação:', err);
        this.close.emit();
      }
    });
  }
}
