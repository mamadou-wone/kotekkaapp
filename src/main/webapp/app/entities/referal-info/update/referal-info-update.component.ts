import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ReferalStatus } from 'app/entities/enumerations/referal-status.model';
import { IReferalInfo } from '../referal-info.model';
import { ReferalInfoService } from '../service/referal-info.service';
import { ReferalInfoFormGroup, ReferalInfoFormService } from './referal-info-form.service';

@Component({
  standalone: true,
  selector: 'jhi-referal-info-update',
  templateUrl: './referal-info-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ReferalInfoUpdateComponent implements OnInit {
  isSaving = false;
  referalInfo: IReferalInfo | null = null;
  referalStatusValues = Object.keys(ReferalStatus);

  protected referalInfoService = inject(ReferalInfoService);
  protected referalInfoFormService = inject(ReferalInfoFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ReferalInfoFormGroup = this.referalInfoFormService.createReferalInfoFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ referalInfo }) => {
      this.referalInfo = referalInfo;
      if (referalInfo) {
        this.updateForm(referalInfo);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const referalInfo = this.referalInfoFormService.getReferalInfo(this.editForm);
    if (referalInfo.id !== null) {
      this.subscribeToSaveResponse(this.referalInfoService.update(referalInfo));
    } else {
      this.subscribeToSaveResponse(this.referalInfoService.create(referalInfo));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReferalInfo>>): void {
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

  protected updateForm(referalInfo: IReferalInfo): void {
    this.referalInfo = referalInfo;
    this.referalInfoFormService.resetForm(this.editForm, referalInfo);
  }
}
