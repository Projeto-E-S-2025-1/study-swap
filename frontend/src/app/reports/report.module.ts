//src/app/reports/report.module.ts
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReportFormComponent } from './report-form/report-form';
import { FormsModule } from '@angular/forms';
import { ReportRoutingModule } from './report-routing.module';

@NgModule({
  declarations: [ReportFormComponent],
  imports: [
    CommonModule,
    FormsModule,
    ReportRoutingModule
  ]
})
export class ReportModule {}
