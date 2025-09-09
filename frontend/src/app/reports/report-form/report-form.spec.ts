//src/app/reports/report-form/report-form.spec.ts
import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReportFormComponent } from './report-form';

describe('ReportFormComponent', () => {
  let component: ReportFormComponent;
  let fixture: ComponentFixture<ReportFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReportFormComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReportFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
