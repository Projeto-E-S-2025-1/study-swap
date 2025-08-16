import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';

import { MaterialService } from '../../services/material.service';
import { Material } from '../../models/material.model';
import { QuestionListComponent } from '../question-list/question-list';
import { AuthService } from '../../../auth/auth.service';

@Component({
  selector: 'app-detail-material',
  standalone: true,
  imports: [CommonModule, RouterModule, QuestionListComponent],
  templateUrl: './detail-material.html',
  styleUrls: ['./detail-material.css']
})
export class DetailMaterial implements OnInit {
  material: Material & { id: number } | null = null;
  isLoading: boolean = true;
  errorMessage: string = '';
  isOwner: boolean = false;
  showMenu: boolean = false;

  toggleMenu(): void {
    this.showMenu = !this.showMenu;
  }

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private materialService: MaterialService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');

    if (idParam) {
      const id = Number(idParam);

      this.materialService.getById(id).subscribe({
        next: (data) => {
          this.material = data as any;
          this.isOwner = this.authService.getUserId() == this.material?.userId;
          this.isLoading = false;
        },
        error: (error) => {
          this.errorMessage = 'Não foi possível carregar o material.';
          this.isLoading = false;
        }
      });
    } else {
      this.errorMessage = 'ID do material não fornecido.';
      this.isLoading = false;
    }
  }

  get materialId(): number | null {
    return this.material ? this.material.id : null;
  }

  apagarMaterial() : void{
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.materialService.delete(id).subscribe({
        next: () => {
        },
        error: (error) => {
          console.error(error.message);
        }
      });
    this.router.navigate(['/material']);
  }
}
