import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUserAffiliation } from '../user-affiliation.model';
import { UserAffiliationService } from '../service/user-affiliation.service';
import { UserAffiliationFormGroup, UserAffiliationFormService } from './user-affiliation-form.service';

@Component({
  standalone: true,
  selector: 'jhi-user-affiliation-update',
  templateUrl: './user-affiliation-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class UserAffiliationUpdateComponent implements OnInit {
  isSaving = false;
  userAffiliation: IUserAffiliation | null = null;

  protected userAffiliationService = inject(UserAffiliationService);
  protected userAffiliationFormService = inject(UserAffiliationFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: UserAffiliationFormGroup = this.userAffiliationFormService.createUserAffiliationFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ userAffiliation }) => {
      this.userAffiliation = userAffiliation;
      if (userAffiliation) {
        this.updateForm(userAffiliation);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const userAffiliation = this.userAffiliationFormService.getUserAffiliation(this.editForm);
    if (userAffiliation.id !== null) {
      this.subscribeToSaveResponse(this.userAffiliationService.update(userAffiliation));
    } else {
      this.subscribeToSaveResponse(this.userAffiliationService.create(userAffiliation));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUserAffiliation>>): void {
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

  protected updateForm(userAffiliation: IUserAffiliation): void {
    this.userAffiliation = userAffiliation;
    this.userAffiliationFormService.resetForm(this.editForm, userAffiliation);
  }
}
