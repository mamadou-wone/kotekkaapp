import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICin } from '../cin.model';
import { CinService } from '../service/cin.service';
import { CinFormGroup, CinFormService } from './cin-form.service';

@Component({
  standalone: true,
  selector: 'jhi-cin-update',
  templateUrl: './cin-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CinUpdateComponent implements OnInit {
  isSaving = false;
  cin: ICin | null = null;

  protected cinService = inject(CinService);
  protected cinFormService = inject(CinFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CinFormGroup = this.cinFormService.createCinFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cin }) => {
      this.cin = cin;
      if (cin) {
        this.updateForm(cin);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cin = this.cinFormService.getCin(this.editForm);
    if (cin.id !== null) {
      this.subscribeToSaveResponse(this.cinService.update(cin));
    } else {
      this.subscribeToSaveResponse(this.cinService.create(cin));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICin>>): void {
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

  protected updateForm(cin: ICin): void {
    this.cin = cin;
    this.cinFormService.resetForm(this.editForm, cin);
  }
}
