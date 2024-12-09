import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { DefaultStatus } from 'app/entities/enumerations/default-status.model';
import { ICard } from '../card.model';
import { CardService } from '../service/card.service';
import { CardFormGroup, CardFormService } from './card-form.service';

@Component({
  standalone: true,
  selector: 'jhi-card-update',
  templateUrl: './card-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CardUpdateComponent implements OnInit {
  isSaving = false;
  card: ICard | null = null;
  defaultStatusValues = Object.keys(DefaultStatus);

  protected cardService = inject(CardService);
  protected cardFormService = inject(CardFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CardFormGroup = this.cardFormService.createCardFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ card }) => {
      this.card = card;
      if (card) {
        this.updateForm(card);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const card = this.cardFormService.getCard(this.editForm);
    if (card.id !== null) {
      this.subscribeToSaveResponse(this.cardService.update(card));
    } else {
      this.subscribeToSaveResponse(this.cardService.create(card));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICard>>): void {
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

  protected updateForm(card: ICard): void {
    this.card = card;
    this.cardFormService.resetForm(this.editForm, card);
  }
}
