import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { App } from 'app/entities/enumerations/app.model';
import { Action } from 'app/entities/enumerations/action.model';
import { IFailedAttempt } from '../failed-attempt.model';
import { FailedAttemptService } from '../service/failed-attempt.service';
import { FailedAttemptFormGroup, FailedAttemptFormService } from './failed-attempt-form.service';

@Component({
  standalone: true,
  selector: 'jhi-failed-attempt-update',
  templateUrl: './failed-attempt-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class FailedAttemptUpdateComponent implements OnInit {
  isSaving = false;
  failedAttempt: IFailedAttempt | null = null;
  appValues = Object.keys(App);
  actionValues = Object.keys(Action);

  protected failedAttemptService = inject(FailedAttemptService);
  protected failedAttemptFormService = inject(FailedAttemptFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: FailedAttemptFormGroup = this.failedAttemptFormService.createFailedAttemptFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ failedAttempt }) => {
      this.failedAttempt = failedAttempt;
      if (failedAttempt) {
        this.updateForm(failedAttempt);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const failedAttempt = this.failedAttemptFormService.getFailedAttempt(this.editForm);
    if (failedAttempt.id !== null) {
      this.subscribeToSaveResponse(this.failedAttemptService.update(failedAttempt));
    } else {
      this.subscribeToSaveResponse(this.failedAttemptService.create(failedAttempt));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFailedAttempt>>): void {
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

  protected updateForm(failedAttempt: IFailedAttempt): void {
    this.failedAttempt = failedAttempt;
    this.failedAttemptFormService.resetForm(this.editForm, failedAttempt);
  }
}
