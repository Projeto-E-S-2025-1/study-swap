import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MaterialType, ConservationStatus, TransactionType } from '../../models/material.model';
import { MaterialService } from '../../services/material.service';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../../auth/auth.service';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Output, EventEmitter } from '@angular/core';
import { Material } from '../../models/material.model';

@Component({
  selector: 'app-update-materials',
  templateUrl: './update-material.html',
  styleUrls: ['./update-material.css'],
  imports: [FormsModule, CommonModule],
  standalone: true
})
export class UpdateMaterial implements OnInit {
  @Output() materialAtualizado = new EventEmitter<Material>();
  materialDTO = {
    title: '' as string | undefined,
    description: '' as string | undefined,
    materialType: MaterialType.LIVRO as MaterialType | undefined,
    conservationStatus: ConservationStatus.NOVO as ConservationStatus | undefined,
    transactionType: TransactionType.DOACAO as TransactionType | undefined,
    price: 0 as number | null | undefined,
    userId: 0 as number | undefined
  };

  material: (Material & { id: number }) | null = null;

  materialFile?: File;
  selectedFile?: File;

  materialTypes = Object.values(MaterialType);
  conservationStatuses = Object.values(ConservationStatus);
  transactionTypes = Object.values(TransactionType);

  successMessage = '';
  errorMessage = '';

  finalMaterial: Partial<Material> = {};

  constructor(
    private route: ActivatedRoute,
    private materialService: MaterialService,
    private router: Router,
    private authService: AuthService,
    private http: HttpClient
  ) {}

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    const id = Number(idParam);
    this.materialService.getById(id).subscribe({
        next: (data) => {
          this.material = data as any;
          this.materialDTO.title = this.material?.title;
          this.materialDTO.description = this.material?.description;
          this.materialDTO.materialType = this.material?.materialType;
          this.materialDTO.conservationStatus = this.material?.conservationStatus;
          this.materialDTO.transactionType = this.material?.transactionType;
          this.materialDTO.price = this.material?.price;
        },
        error: () => {
          this.errorMessage = 'Não foi possível carregar o material.';
        }
      });
    const token = this.authService.getToken();
    if (!token) {
      this.errorMessage = 'Usuário não autenticado. Faça login antes de atualizar um material.';
    }
  }

  onSubmit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    const id = Number(idParam);
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
    
    this.finalMaterial.title = materialToSend.title;
    this.finalMaterial.description = materialToSend.description;
    this.finalMaterial.materialType = materialToSend.materialType;
    this.finalMaterial.conservationStatus = materialToSend.conservationStatus;
    this.finalMaterial.transactionType = materialToSend.transactionType;
    this.finalMaterial.price = materialToSend.price;

    this.materialService.update(id, this.finalMaterial).subscribe({
      next: (response) => {
        this.successMessage = 'Material atualizado com sucesso!';
        setTimeout(() => this.router.navigate(['/material/' + id]), 500)
        this.materialAtualizado.emit(response);
      },
      error: (err) => {
        this.errorMessage = 'Erro ao atualizar o material.';
        console.error(err);
      }
    });
  }  
}
