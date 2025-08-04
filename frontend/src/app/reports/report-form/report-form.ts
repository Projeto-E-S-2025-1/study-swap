import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ReportService } from '../report.service';
import { CreateReportDTO } from '../dto/create-report.dto';

@Component({
  selector: 'app-report-form',
  templateUrl: './report-form.html',
  styleUrls: ['./report-form.css'],
  standalone: false
})
export class ReportFormComponent {
  reason: string = '';
  description: string = '';
  reportedUserId?: number;
  reportedMaterialId?: number;

  reasons = [
    { label: 'Spam', value: 'SPAM' },
    { label: 'Fraude', value: 'FRAUDE' },
    { label: 'Conteúdo impróprio', value: 'CONTEUDO_IMPROPRIO' },
    { label: 'Item não conforme', value: 'ITEM_NAO_CONFORME' }
  ];

  constructor(
    private reportService: ReportService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    const userId = this.route.snapshot.queryParamMap.get('userId');
    const materialId = this.route.snapshot.queryParamMap.get('materialId');

    if (userId) this.reportedUserId = +userId;
    if (materialId) this.reportedMaterialId = +materialId;
  }

  submit() {
    const dto: CreateReportDTO = {
      reason: this.reason as CreateReportDTO['reason'],
      description: this.description
    };

    if (this.reportedUserId) dto.reportedUserId = this.reportedUserId;
    else if (this.reportedMaterialId) dto.reportedMaterialId = this.reportedMaterialId;
    else {
      alert('Nenhum alvo para denúncia foi especificado.');
      return;
    }

    this.reportService.createReport(dto).subscribe({
      next: () => {
        alert('Denúncia enviada com sucesso!');
        this.router.navigateByUrl('/material');
      },
      error: () => alert('Erro ao enviar denúncia.')
    });
  }
}
