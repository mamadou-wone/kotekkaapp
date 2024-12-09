import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IFeatureFlag } from '../feature-flag.model';
import { FeatureFlagService } from '../service/feature-flag.service';
import { FeatureFlagFormGroup, FeatureFlagFormService } from './feature-flag-form.service';

@Component({
  standalone: true,
  selector: 'jhi-feature-flag-update',
  templateUrl: './feature-flag-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class FeatureFlagUpdateComponent implements OnInit {
  isSaving = false;
  featureFlag: IFeatureFlag | null = null;

  protected featureFlagService = inject(FeatureFlagService);
  protected featureFlagFormService = inject(FeatureFlagFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: FeatureFlagFormGroup = this.featureFlagFormService.createFeatureFlagFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ featureFlag }) => {
      this.featureFlag = featureFlag;
      if (featureFlag) {
        this.updateForm(featureFlag);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const featureFlag = this.featureFlagFormService.getFeatureFlag(this.editForm);
    if (featureFlag.id !== null) {
      this.subscribeToSaveResponse(this.featureFlagService.update(featureFlag));
    } else {
      this.subscribeToSaveResponse(this.featureFlagService.create(featureFlag));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFeatureFlag>>): void {
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

  protected updateForm(featureFlag: IFeatureFlag): void {
    this.featureFlag = featureFlag;
    this.featureFlagFormService.resetForm(this.editForm, featureFlag);
  }
}
