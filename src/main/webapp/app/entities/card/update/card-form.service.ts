import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICard, NewCard } from '../card.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICard for edit and NewCardFormGroupInput for create.
 */
type CardFormGroupInput = ICard | PartialWithRequiredKeyOf<NewCard>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ICard | NewCard> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type CardFormRawValue = FormValueOf<ICard>;

type NewCardFormRawValue = FormValueOf<NewCard>;

type CardFormDefaults = Pick<NewCard, 'id' | 'createdDate' | 'lastModifiedDate'>;

type CardFormGroupContent = {
  id: FormControl<CardFormRawValue['id'] | NewCard['id']>;
  uuid: FormControl<CardFormRawValue['uuid']>;
  status: FormControl<CardFormRawValue['status']>;
  label: FormControl<CardFormRawValue['label']>;
  maskedPan: FormControl<CardFormRawValue['maskedPan']>;
  cardHolderName: FormControl<CardFormRawValue['cardHolderName']>;
  token: FormControl<CardFormRawValue['token']>;
  expiryYear: FormControl<CardFormRawValue['expiryYear']>;
  expiryMonth: FormControl<CardFormRawValue['expiryMonth']>;
  rnd: FormControl<CardFormRawValue['rnd']>;
  hash: FormControl<CardFormRawValue['hash']>;
  walletHolder: FormControl<CardFormRawValue['walletHolder']>;
  createdBy: FormControl<CardFormRawValue['createdBy']>;
  createdDate: FormControl<CardFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<CardFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<CardFormRawValue['lastModifiedDate']>;
};

export type CardFormGroup = FormGroup<CardFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CardFormService {
  createCardFormGroup(card: CardFormGroupInput = { id: null }): CardFormGroup {
    const cardRawValue = this.convertCardToCardRawValue({
      ...this.getFormDefaults(),
      ...card,
    });
    return new FormGroup<CardFormGroupContent>({
      id: new FormControl(
        { value: cardRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      uuid: new FormControl(cardRawValue.uuid, {
        validators: [Validators.required],
      }),
      status: new FormControl(cardRawValue.status),
      label: new FormControl(cardRawValue.label, {
        validators: [Validators.maxLength(50)],
      }),
      maskedPan: new FormControl(cardRawValue.maskedPan, {
        validators: [Validators.maxLength(30)],
      }),
      cardHolderName: new FormControl(cardRawValue.cardHolderName, {
        validators: [Validators.maxLength(30)],
      }),
      token: new FormControl(cardRawValue.token, {
        validators: [Validators.maxLength(36)],
      }),
      expiryYear: new FormControl(cardRawValue.expiryYear, {
        validators: [Validators.maxLength(2)],
      }),
      expiryMonth: new FormControl(cardRawValue.expiryMonth, {
        validators: [Validators.maxLength(2)],
      }),
      rnd: new FormControl(cardRawValue.rnd, {
        validators: [Validators.maxLength(20)],
      }),
      hash: new FormControl(cardRawValue.hash, {
        validators: [Validators.maxLength(100)],
      }),
      walletHolder: new FormControl(cardRawValue.walletHolder),
      createdBy: new FormControl(cardRawValue.createdBy),
      createdDate: new FormControl(cardRawValue.createdDate),
      lastModifiedBy: new FormControl(cardRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(cardRawValue.lastModifiedDate),
    });
  }

  getCard(form: CardFormGroup): ICard | NewCard {
    return this.convertCardRawValueToCard(form.getRawValue() as CardFormRawValue | NewCardFormRawValue);
  }

  resetForm(form: CardFormGroup, card: CardFormGroupInput): void {
    const cardRawValue = this.convertCardToCardRawValue({ ...this.getFormDefaults(), ...card });
    form.reset(
      {
        ...cardRawValue,
        id: { value: cardRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CardFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertCardRawValueToCard(rawCard: CardFormRawValue | NewCardFormRawValue): ICard | NewCard {
    return {
      ...rawCard,
      createdDate: dayjs(rawCard.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawCard.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertCardToCardRawValue(
    card: ICard | (Partial<NewCard> & CardFormDefaults),
  ): CardFormRawValue | PartialWithRequiredKeyOf<NewCardFormRawValue> {
    return {
      ...card,
      createdDate: card.createdDate ? card.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: card.lastModifiedDate ? card.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
