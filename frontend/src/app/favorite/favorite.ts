import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';

import { Material } from '../materials/models/material.model';
import { FavoriteService } from './favorite.service';
import { RouterModule } from '@angular/router';


@Component({
  selector: 'app-favorite-materials',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './favorite.html',
  styleUrls: ['./favorite.css']
})
export class Favorite implements OnInit {
  materiais: Material[] = [];
  isLoading: boolean = true;

  constructor(private favoriteService: FavoriteService) {}

  ngOnInit(): void {
    this.carregarFavoritos();
    this.isLoading = false;
  }

  carregarFavoritos() : void{
    this.favoriteService.getFavorites().subscribe({
      next: (data) => {
        this.materiais = data;
        console.log(data);
      },
      error: (err) => {
        console.error(err);
      }
    });
  }
}
