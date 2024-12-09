import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { DefaultStatus } from 'app/entities/enumerations/default-status.model';
import { IRecipient } from '../recipient.model';
import { RecipientService } from '../service/recipient.service';
import { RecipientFormGroup, RecipientFormService } from './recipient-form.service';

@Component({
  standalone: true,
  selector: 'jhi-recipient-update',
  templateUrl: './recipient-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class RecipientUpdateComponent implements OnInit {
  isSaving = false;
  recipient: IRecipient | null = null;
  defaultStatusValues = Object.keys(DefaultStatus);

  protected recipientService = inject(RecipientService);
  protected recipientFormService = inject(RecipientFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: RecipientFormGroup = this.recipientFormService.createRecipientFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ recipient }) => {
      this.recipient = recipient;
      if (recipient) {
        this.updateForm(recipient);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const recipient = this.recipientFormService.getRecipient(this.editForm);
    if (recipient.id !== null) {
      this.subscribeToSaveResponse(this.recipientService.update(recipient));
    } else {
      this.subscribeToSaveResponse(this.recipientService.create(recipient));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRecipient>>): void {
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

  protected updateForm(recipient: IRecipient): void {
    this.recipient = recipient;
    this.recipientFormService.resetForm(this.editForm, recipient);
  }
}
