//src/app/materials/components/question-list/question-list.ts
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

  // Variáveis para exclusão
  showDeleteModal = false;
  questionToDelete: Question | null = null;
  isDeleting = false;
  deleteErrorMessage = '';

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

  confirmDelete(question: Question): void {
    this.questionToDelete = question;
    this.showDeleteModal = true;
    this.deleteErrorMessage = '';
  }

  cancelDelete(): void {
    this.showDeleteModal = false;
    this.questionToDelete = null;
    this.deleteErrorMessage = '';
  }

  deleteQuestion(): void {
    if (!this.questionToDelete) {
      this.deleteErrorMessage = 'Erro interno. Tente novamente.';
      return;
    }

    this.isDeleting = true;
    this.deleteErrorMessage = '';

    this.questionService.delete(this.questionToDelete.id).subscribe({
      next: () => {
        this.questions = this.questions.filter(q => q.id !== this.questionToDelete!.id);

        this.showDeleteModal = false;
        this.questionToDelete = null;
        this.isDeleting = false;

        this.successMessage = 'Pergunta excluída com sucesso!';
        setTimeout(() => {
          this.successMessage = '';
        }, 3000);
      },
      error: (err) => {
        console.error('Erro ao excluir pergunta:', err);
        this.deleteErrorMessage = 'Erro ao excluir pergunta. Tente novamente.';
        this.isDeleting = false;
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
        this.questions.unshift(newQuestion);
        this.resetForm();
        this.isSubmitting = false;

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

  canEditQuestion(question: Question): boolean {
    const currentUserId = this.authService.getUserId();

    const authorId = question.authorId ?? question.author?.id;
    return currentUserId !== null && authorId === currentUserId;
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
        const index = this.questions.findIndex(q => q.id === updatedQuestion.id);
        if (index !== -1) {
          this.questions[index] = updatedQuestion;
        }

        this.editSuccessMessage = 'Pergunta atualizada com sucesso!';
        this.isUpdating = false;

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
