import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';

import { Material, MaterialType } from '../../models/material.model';
import { MaterialService } from '../../services/material.service';
import { FormMaterial } from '../form-material/form-material';

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
  isLoading: boolean = true;
  errorMessage: string = '';

  showModal: boolean = false;
  isFiltered: boolean = false;

  textFilter: string = '';
  typeFilter: string = '';

  constructor(private materialService: MaterialService) {}

  ngOnInit(): void {
    this.isFiltered = false;
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

  carregarMateriaisFiltrados(titulo: string, tipo: string): void {
    
    this.isLoading = true;
    let filtro = '';
    filtro += 'title=' + titulo.trim() + '&materialtype=' + tipo;

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
