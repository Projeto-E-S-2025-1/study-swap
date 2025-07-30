import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';

import { MaterialService } from '../../services/material.service';
import { Material } from '../../models/material.model';

@Component({
  selector: 'app-detail-material',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './detail-material.html',
  styleUrls: ['./detail-material.css']
})
export class DetailMaterial implements OnInit {
  material: Material | null = null;
  isLoading: boolean = true;
  errorMessage: string = '';

  constructor(
    private route: ActivatedRoute,
    private materialService: MaterialService
  ) {}

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');

    if (idParam) {
      const id = Number(idParam);

      this.materialService.getById(id).subscribe({
        next: (data) => {
          this.material = data;
          this.isLoading = false;
        },
        error: (error) => {
          console.error('Erro ao buscar material:', error);
          this.errorMessage = 'Não foi possível carregar o material.';
          this.isLoading = false;
        }
      });
    } else {
      this.errorMessage = 'ID do material não fornecido.';
      this.isLoading = false;
    }
  }
}
