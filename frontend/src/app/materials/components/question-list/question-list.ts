import { Component, Input, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Question } from '../../models/question.model';
import { QuestionService, CreateQuestionDTO, UpdateQuestionDTO } from '../../services/question.service';
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

  // Variáveis para edição
  editingQuestionId: number | null = null;
  isUpdating = false;
  editQuestionDTO: UpdateQuestionDTO = {
    title: '',
    description: ''
  };

  questionDTO: CreateQuestionDTO = {
    title: '',
    description: '',
    materialId: 0
  };

  successMessage = '';
  errorMessage = '';
  editSuccessMessage = '';
  editErrorMessage = '';

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

  // Métodos para edição
  canEditQuestion(question: Question): boolean {
    const currentUserId = this.authService.getUserId();
    return currentUserId !== null && question.authorId === currentUserId;
  }

  startEdit(question: Question): void {
    this.editingQuestionId = question.id;
    this.editQuestionDTO = {
      title: question.title,
      description: question.description
    };
    this.clearEditMessages();
  }

  cancelEdit(): void {
    this.editingQuestionId = null;
    this.editQuestionDTO = { title: '', description: '' };
    this.clearEditMessages();
  }

  updateQuestion(): void {
    this.clearEditMessages();

    if (!this.editQuestionDTO.title?.trim() || !this.editQuestionDTO.description?.trim()) {
      this.editErrorMessage = 'Por favor, preencha todos os campos.';
      return;
    }

    if (this.editingQuestionId === null) {
      this.editErrorMessage = 'Erro interno. Tente novamente.';
      return;
    }

    this.isUpdating = true;

    this.questionService.update(this.editingQuestionId, this.editQuestionDTO).subscribe({
      next: (updatedQuestion) => {
        // Atualizar a pergunta na lista
        const index = this.questions.findIndex(q => q.id === updatedQuestion.id);
        if (index !== -1) {
          this.questions[index] = updatedQuestion;
        }

        this.editSuccessMessage = 'Pergunta atualizada com sucesso!';
        this.isUpdating = false;

        // Sair do modo de edição após 1.5 segundos
        setTimeout(() => {
          this.cancelEdit();
        }, 1500);
      },
      error: (err) => {
        console.error('Erro ao atualizar pergunta:', err);
        this.editErrorMessage = 'Erro ao atualizar pergunta. Tente novamente.';
        this.isUpdating = false;
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

  private clearEditMessages(): void {
    this.editSuccessMessage = '';
    this.editErrorMessage = '';
  }
}
