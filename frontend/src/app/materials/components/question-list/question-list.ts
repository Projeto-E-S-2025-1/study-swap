import { Component, Input, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Question } from '../../models/question.model';
import { QuestionService, CreateQuestionDTO } from '../../services/question.service';
import { AuthService } from '../../../auth/auth.service';

@Component({
  selector: 'app-question-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './question-list.html',
  styleUrls: ['./question-list.css']
})
export class QuestionListComponent implements OnInit {
  @Input() materialId!: number;
  questions: Question[] = [];
  isLoading = true;
  showForm = false;
  isSubmitting = false;

  questionDTO: CreateQuestionDTO = {
    title: '',
    description: '',
    materialId: 0
  };

  successMessage = '';
  errorMessage = '';

  constructor(
    private questionService: QuestionService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.loadQuestions();
  }

  loadQuestions(): void {
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

  toggleForm(): void {
    this.showForm = !this.showForm;
    if (this.showForm) {
      this.resetForm();
      this.clearMessages();
    }
  }

  onSubmit(): void {
    this.clearMessages();

    // Verificar se o usuário está autenticado
    const token = this.authService.getToken();
    if (!token) {
      this.errorMessage = 'Você precisa estar logado para fazer uma pergunta.';
      return;
    }

    if (!this.questionDTO.title.trim() || !this.questionDTO.description.trim()) {
      this.errorMessage = 'Por favor, preencha todos os campos.';
      return;
    }

    this.isSubmitting = true;
    this.questionDTO.materialId = this.materialId;

    this.questionService.create(this.questionDTO).subscribe({
      next: (newQuestion) => {
        this.successMessage = 'Pergunta enviada com sucesso!';
        this.questions.unshift(newQuestion); // Adiciona no início da lista
        this.resetForm();
        this.isSubmitting = false;

        // Ocultar formulário após 2 segundos
        setTimeout(() => {
          this.showForm = false;
          this.clearMessages();
        }, 2000);
      },
      error: (err) => {
        console.error('Erro ao criar pergunta:', err);
        this.errorMessage = 'Erro ao enviar pergunta. Tente novamente.';
        this.isSubmitting = false;
      }
    });
  }

  private resetForm(): void {
    this.questionDTO = {
      title: '',
      description: '',
      materialId: this.materialId
    };
  }

  private clearMessages(): void {
    this.successMessage = '';
    this.errorMessage = '';
  }
}
