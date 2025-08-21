import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CreateReportDTO } from './dto/create-report.dto';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ReportService {

  private apiUrl = `${environment.apiUrl}/report`;

  constructor(private http: HttpClient) {}

  createReport(dto: CreateReportDTO): Observable<any> {
    return this.http.post(this.apiUrl, dto);
  }
}
