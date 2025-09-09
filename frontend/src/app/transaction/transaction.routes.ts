//src/app/transaction/transaction.routes.ts
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { TransactionsComponent } from './components/list-user-transactions/list-user-transactions';
import { ReviewFormComponent } from './components/review-transaction/review-transaction.component';

export const TRANSACTION_ROUTES: Routes = [
    {path: '', component: TransactionsComponent},
    {path: 'review/:id', component: ReviewFormComponent}
]