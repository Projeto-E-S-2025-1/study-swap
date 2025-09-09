//src/app/materials/models/question.model.ts
export interface Question {
  id: number;
  title: string;
  description: string;
  author?: {
    id: number;
    name: string;
  };
  authorName: string;
  authorId: number;
  createdAt?: string;
}

