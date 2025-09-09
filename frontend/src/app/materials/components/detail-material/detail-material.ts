//src/app/materials/components/detail-material/detail-material.ts
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';

import { MaterialService } from '../../services/material.service';
import { Material } from '../../models/material.model';
import { QuestionListComponent } from '../question-list/question-list';
import { AuthService } from '../../../auth/auth.service';
import { environment } from '../../../../environments/environment';
import { UpdateMaterial } from '../update-material/update-material';
import { OpenTransactionComponent } from '../../../transaction/components/open-transaction/open-transaction.component';
import { TransactionProposalsComponent } from '../../../transaction/components/list-options/list-transactions';
import { TransactionService } from '../../../transaction/services/transaction.service';
import { FavoriteService } from '../../../favorite/favorite.service';
import { UserAverage } from '../../../transaction/models/review.model';
import { ReviewService } from '../../../transaction/services/review.service';

@Component({
  selector: 'app-detail-material',
  standalone: true,
  imports: [CommonModule, RouterModule, QuestionListComponent, UpdateMaterial, OpenTransactionComponent, TransactionProposalsComponent],
  templateUrl: './detail-material.html',
  styleUrls: ['./detail-material.css']
})
export class DetailMaterial implements OnInit {
  material: (Material & { id: number }) | null = null;
  rating!: UserAverage;
  isLoading: boolean = true;
  errorMessage: string = '';
  isFavorite: boolean = false;
  isOwner: boolean = false;
  isDone: boolean = false;
  showMenu: boolean = false;
  showModalUpdate: boolean = false;
  showModalTransaction: boolean = false;

  apiUrl = environment.apiUrl;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private reviewService: ReviewService,
    private materialService: MaterialService,
    private authService: AuthService,
    private favoriteService: FavoriteService
  ) {}

  toggleMenu(): void {
    this.showMenu = !this.showMenu;
  }

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');

    if (idParam) {
      const id = Number(idParam);

      this.materialService.getById(id).subscribe({
        next: (data) => {
          this.material = data as any;
          this.isOwner = this.authService.getUserId() == this.material?.userId;
          this.isLoading = false;
          this.loadRating();
        },
        error: () => {
          this.errorMessage = 'Não foi possível carregar o material.';
          this.isLoading = false;
        }
      });
    } else {
      this.errorMessage = 'ID do material não fornecido.';
      this.isLoading = false;
    }

    this.updateFavorited();
  }

  loadRating(): void {
    const userId = this.material?.userId;
    if (userId == null) return;

    this.reviewService.getUserRating(userId).subscribe({
      next: (res: UserAverage) => {
        this.rating = res;
      },
      error: (err) => {
        console.error("Erro ao carregar rating:", err);
      }
    });
  }

  get materialId(): number | null {
    return this.material ? this.material.id : null;
  }

  apagarMaterial(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.materialService.delete(id).subscribe({
        next: () => {
        },
        error: (error) => {
          console.error(error.message);
        }
      });
    setTimeout(() => this.router.navigate(['/material']), 250);
  }

  closeModal(): void {
    this.showModalUpdate = false;
    this.showModalTransaction = false;
  }

  favorite(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.favoriteService.favoriteMaterial(id).subscribe({
      next: () => {},
      error: (err) => {
        console.error(err);
      }
    });
    setTimeout(() => this.router.navigate(['/material/' + id]), 500);
    this.isFavorite = true;
  }

  unfavorite(): void{
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.favoriteService.unfavoriteMaterial(id).subscribe({
      next: () => {},
      error: (err) => {
        console.error(err);
      }
    });
    setTimeout(() => this.router.navigate(['/material/' + id]), 500);
    this.isFavorite = false;
  }

  updateFavorited(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));

    this.favoriteService.getFavorites().subscribe({
      next: (data) => {
        var flag = false;
        for(const atom of data){
          if(atom.id == id){
            flag = true;
          }
        }
        if(flag) {
          this.isFavorite = true;
        } else {
          this.isFavorite = false;
        }
      },
      error: (err) => {
        console.error(err);
      }
    });
  }

  onMaterialAtualizado(novoMaterial: Material): void {
    this.closeModal();
    const id = Number(novoMaterial.id);
    this.materialService.getById(id).subscribe({
        next: (data) => {
          this.material = data as any;
          this.isOwner = this.authService.getUserId() == this.material?.userId;
          this.isLoading = false;
        },
        error: () => {
          this.errorMessage = 'Não foi possível carregar o material.';
          this.isLoading = false;
        }
      });
  }

  onTransactionDone() {
    this.isDone = true;  
    this.showModalTransaction = false;
  }

  onCancelTransaction() {
    this.isDone = false;
    this.showModalTransaction = false;
  }
}
