import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUserAccess } from '../user-access.model';
import { UserAccessService } from '../service/user-access.service';
import { UserAccessFormGroup, UserAccessFormService } from './user-access-form.service';

@Component({
  standalone: true,
  selector: 'jhi-user-access-update',
  templateUrl: './user-access-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class UserAccessUpdateComponent implements OnInit {
  isSaving = false;
  userAccess: IUserAccess | null = null;

  protected userAccessService = inject(UserAccessService);
  protected userAccessFormService = inject(UserAccessFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: UserAccessFormGroup = this.userAccessFormService.createUserAccessFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ userAccess }) => {
      this.userAccess = userAccess;
      if (userAccess) {
        this.updateForm(userAccess);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const userAccess = this.userAccessFormService.getUserAccess(this.editForm);
    if (userAccess.id !== null) {
      this.subscribeToSaveResponse(this.userAccessService.update(userAccess));
    } else {
      this.subscribeToSaveResponse(this.userAccessService.create(userAccess));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUserAccess>>): void {
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

  protected updateForm(userAccess: IUserAccess): void {
    this.userAccess = userAccess;
    this.userAccessFormService.resetForm(this.editForm, userAccess);
  }
}
