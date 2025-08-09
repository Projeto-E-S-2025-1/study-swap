import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MaterialType, ConservationStatus, TransactionType } from '../../models/material.model';
import { MaterialService } from '../../services/material.service';
import { Router } from '@angular/router';
import { AuthService } from '../../../auth/auth.service';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Output, EventEmitter } from '@angular/core';
import { Material } from '../../models/material.model';

@Component({
  selector: 'app-form-materials',
  templateUrl: './form-material.html',
  styleUrls: ['./form-material.css'],
  imports: [FormsModule, CommonModule],
  standalone: true
})
export class FormMaterial implements OnInit {
  @Output() materialAdicionado = new EventEmitter<Material>();
  materialDTO = {
    title: '',
    description: '',
    materialType: MaterialType.LIVRO,
    conservationStatus: ConservationStatus.NOVO,
    transactionType: TransactionType.DOACAO,
    price: 0 as number | null,
    userId: 0 as number
  };

  materialFile?: File;
  selectedFile?: File;
  imagePreview?: string;

  materialTypes = Object.values(MaterialType);
  conservationStatuses = Object.values(ConservationStatus);
  transactionTypes = Object.values(TransactionType);

  successMessage = '';
  errorMessage = '';

  constructor(
    private materialService: MaterialService,
    private router: Router,
    private authService: AuthService,
    private http: HttpClient
  ) {}

  ngOnInit(): void {
    const token = this.authService.getToken();
    if (!token) {
      this.errorMessage = 'Usuário não autenticado. Faça login antes de cadastrar um material.';
    }
  }

  onFileSelected(event: any): void {
    const file = event.target.files[0];
    if (file) {
      this.selectedFile = file;
      
      // Criar preview da imagem
      const reader = new FileReader();
      reader.onload = (e: any) => {
        this.imagePreview = e.target.result;
      };
      reader.readAsDataURL(file);
    }
  }

  onSubmit(): void {
    this.successMessage = '';
    this.errorMessage = '';
  
    if (!this.materialDTO.title) {
      this.errorMessage = 'Preencha todos os campos obrigatórios.';
      return;
    }

    // Ajusta o preço baseado no tipo de transação
    const materialToSend = { ...this.materialDTO };
    if (materialToSend.transactionType !== TransactionType.VENDA) {
      materialToSend.price = null;
    }
    materialToSend.userId = Number(this.authService.getUserId());
    console.log(materialToSend.userId);
  
    this.materialService.create(materialToSend, this.selectedFile).subscribe({
      next: () => {
        this.successMessage = 'Material cadastrado com sucesso!';
        setTimeout(() => this.router.navigate(['/material']), 1500);
        this.materialAdicionado.emit(materialToSend);
      },
      error: (err) => {
        this.errorMessage = 'Erro ao cadastrar o material.';
        console.error(err);
      }
    });
  }  
}
