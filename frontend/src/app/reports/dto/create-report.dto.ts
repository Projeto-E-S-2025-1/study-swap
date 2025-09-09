//src/app/reports/dto/create-report.dto.ts
export type ReportReason = 'SPAM' | 'CONTEUDO_IMPROPRIO' | 'FRAUDE' | 'ITEM_NAO_CONFORME';

export interface CreateReportDTO {
  reportedUserId?: number;
  reportedMaterialId?: number;
  reason: ReportReason;
  description?: string;
}