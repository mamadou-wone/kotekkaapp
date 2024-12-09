import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { App } from 'app/entities/enumerations/app.model';
import { Action } from 'app/entities/enumerations/action.model';
import { IFailedAttemptHistory } from '../failed-attempt-history.model';
import { FailedAttemptHistoryService } from '../service/failed-attempt-history.service';
import { FailedAttemptHistoryFormGroup, FailedAttemptHistoryFormService } from './failed-attempt-history-form.service';

@Component({
  standalone: true,
  selector: 'jhi-failed-attempt-history-update',
  templateUrl: './failed-attempt-history-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class FailedAttemptHistoryUpdateComponent implements OnInit {
  isSaving = false;
  failedAttemptHistory: IFailedAttemptHistory | null = null;
  appValues = Object.keys(App);
  actionValues = Object.keys(Action);

  protected failedAttemptHistoryService = inject(FailedAttemptHistoryService);
  protected failedAttemptHistoryFormService = inject(FailedAttemptHistoryFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: FailedAttemptHistoryFormGroup = this.failedAttemptHistoryFormService.createFailedAttemptHistoryFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ failedAttemptHistory }) => {
      this.failedAttemptHistory = failedAttemptHistory;
      if (failedAttemptHistory) {
        this.updateForm(failedAttemptHistory);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const failedAttemptHistory = this.failedAttemptHistoryFormService.getFailedAttemptHistory(this.editForm);
    if (failedAttemptHistory.id !== null) {
      this.subscribeToSaveResponse(this.failedAttemptHistoryService.update(failedAttemptHistory));
    } else {
      this.subscribeToSaveResponse(this.failedAttemptHistoryService.create(failedAttemptHistory));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFailedAttemptHistory>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(failedAttemptHistory: IFailedAttemptHistory): void {
    this.failedAttemptHistory = failedAttemptHistory;
    this.failedAttemptHistoryFormService.resetForm(this.editForm, failedAttemptHistory);
  }
}
