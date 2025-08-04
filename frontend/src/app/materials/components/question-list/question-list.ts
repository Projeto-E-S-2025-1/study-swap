import { Component, Input, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Question } from '../../models/question.model';
import { QuestionService } from '../../services/question.service';


@Component({
  selector: 'app-question-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './question-list.html',
  styleUrls: ['./question-list.css']
})
export class QuestionListComponent implements OnInit {
  @Input() materialId!: number;
  questions: Question[] = [];
  isLoading = true;

  constructor(private questionService: QuestionService) {}

  ngOnInit(): void {
    this.questionService.getByMaterialId(this.materialId).subscribe({
      next: (questions) => {
        this.questions = questions;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Erro ao carregar perguntas', err);
        this.isLoading = false;
      }
    });
  }
}
