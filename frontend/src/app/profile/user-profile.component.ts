//src/app/profile/user-profile.component.ts
import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

interface Review {
  reviewer: string;
  comment: string;
  rating: number;
}

@Component({
  selector: 'app-user-profile',
  templateUrl: 'user-profile.component.html',
  styleUrls: ['user-profile.component.css'],
  imports: [CommonModule]
})
export class UserProfileComponent {
  @Input() name: string = "João da Silva";
  @Input() description: string = "Professor universitário apaixonado por tecnologia e ensino.";
  @Input() reviews: Review[] = [
    { reviewer: "Maria", comment: "Excelente explicação, muito claro e paciente!", rating: 5 },
    { reviewer: "Carlos", comment: "Gostei muito, ajudou bastante nos estudos.", rating: 4 },
    { reviewer: "Ana", comment: "Ótimo conteúdo, mas poderia ser mais detalhado.", rating: 3 }
  ];
}
