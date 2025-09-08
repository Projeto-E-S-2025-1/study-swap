//src/app/reports/report-routing.module.ts
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ReportFormComponent } from './report-form/report-form';

const routes: Routes = [
  { path: '', component: ReportFormComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ReportRoutingModule {}
