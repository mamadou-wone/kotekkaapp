import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { OtpStatus } from 'app/entities/enumerations/otp-status.model';
import { IOneTimePassword } from '../one-time-password.model';
import { OneTimePasswordService } from '../service/one-time-password.service';
import { OneTimePasswordFormGroup, OneTimePasswordFormService } from './one-time-password-form.service';

@Component({
  standalone: true,
  selector: 'jhi-one-time-password-update',
  templateUrl: './one-time-password-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class OneTimePasswordUpdateComponent implements OnInit {
  isSaving = false;
  oneTimePassword: IOneTimePassword | null = null;
  otpStatusValues = Object.keys(OtpStatus);

  protected oneTimePasswordService = inject(OneTimePasswordService);
  protected oneTimePasswordFormService = inject(OneTimePasswordFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: OneTimePasswordFormGroup = this.oneTimePasswordFormService.createOneTimePasswordFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ oneTimePassword }) => {
      this.oneTimePassword = oneTimePassword;
      if (oneTimePassword) {
        this.updateForm(oneTimePassword);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const oneTimePassword = this.oneTimePasswordFormService.getOneTimePassword(this.editForm);
    if (oneTimePassword.id !== null) {
      this.subscribeToSaveResponse(this.oneTimePasswordService.update(oneTimePassword));
    } else {
      this.subscribeToSaveResponse(this.oneTimePasswordService.create(oneTimePassword));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOneTimePassword>>): void {
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

  protected updateForm(oneTimePassword: IOneTimePassword): void {
    this.oneTimePassword = oneTimePassword;
    this.oneTimePasswordFormService.resetForm(this.editForm, oneTimePassword);
  }
}
