import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

import { Material } from '../../models/material.model';
import { MaterialService } from '../../services/material.service';
import { FormMaterial } from '../form-material/form-material';

@Component({
  selector: 'app-list-materials',
  standalone: true,
  imports: [CommonModule, RouterModule, FormMaterial],
  templateUrl: './list-material.html',
  styleUrls: ['./list-material.css']
})
export class ListMaterial implements OnInit {
  materials: Material[] = [];
  isLoading: boolean = true;
  errorMessage: string = '';

  showModal: boolean = false;

  constructor(private materialService: MaterialService) {}

  ngOnInit(): void {
    this.carregarMateriais();
  }

  carregarMateriais(): void {
    this.materialService.getAll().subscribe({
      next: (data) => {
        this.materials = data;
        this.isLoading = false;
      },
      error: (err) => {
        this.errorMessage = 'Erro ao carregar materiais.';
        this.isLoading = false;
      }
    });
  }

  openModal(): void {
    this.showModal = true;
  }

  closeModal(): void {
    this.showModal = false;
  }

  onProdutoAdicionado(novoMaterial: Material): void {
    this.materials.push(novoMaterial);
    this.closeModal();
  }
}
