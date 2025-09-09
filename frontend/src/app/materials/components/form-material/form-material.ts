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
    userId: 0 as number,
    available: true
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
      const allowedTypes = ['image/jpeg', 'image/png', 'image/webp']; // tipos permitidos
      const maxSizeMB = 5; // tamanho máximo em MB
      const maxSizeBytes = maxSizeMB * 1024 * 1024;

      // Validação do tipo
      if (!allowedTypes.includes(file.type)) {
        this.errorMessage = 'Tipo de arquivo inválido. Apenas JPG, PNG ou WebP são permitidos.';
        this.selectedFile = undefined;
        this.imagePreview = undefined;
        return;
      }

      // Validação do tamanho
      if (file.size > maxSizeBytes) {
        this.errorMessage = `Arquivo muito grande. Máximo permitido: ${maxSizeMB}MB.`;
        this.selectedFile = undefined;
        this.imagePreview = undefined;
        return;
      }

      // Se passou nas validações, define o arquivo
      this.selectedFile = file;
      this.errorMessage = '';

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

    this.materialService.create(materialToSend, this.selectedFile).subscribe({
      next: (materialCadastrado) => {
        this.successMessage = 'Material cadastrado com sucesso!';
        this.materialAdicionado.emit(materialCadastrado); // <-- objeto correto
        setTimeout(() => this.router.navigate(['/material']), 1500);
      },
      error: (err) => {
        this.errorMessage = 'Erro ao cadastrar o material.';
        console.error(err);
      }
    });
  }
}
