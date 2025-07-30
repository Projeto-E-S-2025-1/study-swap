import { Routes } from '@angular/router';
import { ListMaterial } from './components/list-material/list-material';
import { FormMaterial } from './components/form-material/form-material';
import { DetailMaterial } from './components/detail-material/detail-material';

export const MATERIALS_ROUTES: Routes = [
  { path: '', component: ListMaterial },
  { path: 'new', component: FormMaterial },
  { path: 'edit/:id', component: FormMaterial },
  { path: ':id', component: DetailMaterial }
];