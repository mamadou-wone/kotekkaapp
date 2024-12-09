import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { RequestStatus } from 'app/entities/enumerations/request-status.model';
import { IMoneyRequest } from '../money-request.model';
import { MoneyRequestService } from '../service/money-request.service';
import { MoneyRequestFormGroup, MoneyRequestFormService } from './money-request-form.service';

@Component({
  standalone: true,
  selector: 'jhi-money-request-update',
  templateUrl: './money-request-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MoneyRequestUpdateComponent implements OnInit {
  isSaving = false;
  moneyRequest: IMoneyRequest | null = null;
  requestStatusValues = Object.keys(RequestStatus);

  protected moneyRequestService = inject(MoneyRequestService);
  protected moneyRequestFormService = inject(MoneyRequestFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: MoneyRequestFormGroup = this.moneyRequestFormService.createMoneyRequestFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ moneyRequest }) => {
      this.moneyRequest = moneyRequest;
      if (moneyRequest) {
        this.updateForm(moneyRequest);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const moneyRequest = this.moneyRequestFormService.getMoneyRequest(this.editForm);
    if (moneyRequest.id !== null) {
      this.subscribeToSaveResponse(this.moneyRequestService.update(moneyRequest));
    } else {
      this.subscribeToSaveResponse(this.moneyRequestService.create(moneyRequest));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMoneyRequest>>): void {
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

  protected updateForm(moneyRequest: IMoneyRequest): void {
    this.moneyRequest = moneyRequest;
    this.moneyRequestFormService.resetForm(this.editForm, moneyRequest);
  }
}
