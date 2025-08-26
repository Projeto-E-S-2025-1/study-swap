import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';

import { ConservationStatus, Material, MaterialType, TransactionType } from '../../models/material.model';
import { MaterialService } from '../../services/material.service';
import { FormMaterial } from '../form-material/form-material';
import { environment } from '../../../../environments/environment';

@Component({
  selector: 'app-list-materials',
  standalone: true,
  imports: [CommonModule, RouterModule, FormMaterial, FormsModule],
  templateUrl: './list-material.html',
  styleUrls: ['./list-material.css']
})
export class ListMaterial implements OnInit {
  materials: Material[] = [];
  materialTypes = Object.values(MaterialType);
  conservationStatus = Object.values(ConservationStatus);
  transactionTypes = Object.values(TransactionType);
  isLoading: boolean = true;
  errorMessage: string = '';

  showModal: boolean = false;
  isFiltered: boolean = false;

  textFilter: string = '';
  typeFilter: string = '';
  conservationFilter: string = '';
  transactionFilter: string = '';

  apiUrl = environment.apiUrl;

  constructor(private materialService: MaterialService) {}

  ngOnInit(): void {
    this.isFiltered = false;
    this.carregarMateriais();
  }

  carregarMateriais(): void {
    this.materialService.getAvailableMaterials().subscribe({
      next: (data) => {
        this.materials = data;
        this.isLoading = false;
      },
      error: () => {
        this.errorMessage = 'Erro ao carregar materiais.';
        this.isLoading = false;
      }
    });
  }

  carregarMateriaisFiltrados(titulo: string, tipo: string, conservation: string, transaction: string): void {
    this.isLoading = true;
    let filtro = '';
    filtro += 'title=' + titulo.trim() + '&materialType=' + tipo + '&conservationStatus=' + conservation + '&transactionType=' + transaction;

    if(filtro.trim() == ''){
      this.isFiltered = false;
    } else {
      this.materialService.getByFilter(filtro).subscribe({
        next: data => {
          this.materials = data;
          this.isLoading = false;
        },
        error: (err) => {
          this.errorMessage = 'Erro ao carregar materiais.';
          this.isLoading = false;
        }
      });
      this.isFiltered = true;
    }
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
