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

