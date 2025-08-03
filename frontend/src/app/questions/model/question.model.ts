import { Material } from '../../materials/models/material.model';
import { User } from '../../models/user.model';

export interface Question {
  id?: number;
  title: string;
  description: string;
  author: User;
  material: Material;
  createdAt?: string;
}
