import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IRecipient, NewRecipient } from '../recipient.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IRecipient for edit and NewRecipientFormGroupInput for create.
 */
type RecipientFormGroupInput = IRecipient | PartialWithRequiredKeyOf<NewRecipient>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IRecipient | NewRecipient> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type RecipientFormRawValue = FormValueOf<IRecipient>;

type NewRecipientFormRawValue = FormValueOf<NewRecipient>;

type RecipientFormDefaults = Pick<NewRecipient, 'id' | 'createdDate' | 'lastModifiedDate'>;

type RecipientFormGroupContent = {
  id: FormControl<RecipientFormRawValue['id'] | NewRecipient['id']>;
  uuid: FormControl<RecipientFormRawValue['uuid']>;
  status: FormControl<RecipientFormRawValue['status']>;
  firstName: FormControl<RecipientFormRawValue['firstName']>;
  lastName: FormControl<RecipientFormRawValue['lastName']>;
  phoneNumber: FormControl<RecipientFormRawValue['phoneNumber']>;
  walletHolder: FormControl<RecipientFormRawValue['walletHolder']>;
  createdBy: FormControl<RecipientFormRawValue['createdBy']>;
  createdDate: FormControl<RecipientFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<RecipientFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<RecipientFormRawValue['lastModifiedDate']>;
};

export type RecipientFormGroup = FormGroup<RecipientFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class RecipientFormService {
  createRecipientFormGroup(recipient: RecipientFormGroupInput = { id: null }): RecipientFormGroup {
    const recipientRawValue = this.convertRecipientToRecipientRawValue({
      ...this.getFormDefaults(),
      ...recipient,
    });
    return new FormGroup<RecipientFormGroupContent>({
      id: new FormControl(
        { value: recipientRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      uuid: new FormControl(recipientRawValue.uuid, {
        validators: [Validators.required],
      }),
      status: new FormControl(recipientRawValue.status),
      firstName: new FormControl(recipientRawValue.firstName, {
        validators: [Validators.maxLength(50)],
      }),
      lastName: new FormControl(recipientRawValue.lastName, {
        validators: [Validators.maxLength(50)],
      }),
      phoneNumber: new FormControl(recipientRawValue.phoneNumber, {
        validators: [Validators.maxLength(13)],
      }),
      walletHolder: new FormControl(recipientRawValue.walletHolder),
      createdBy: new FormControl(recipientRawValue.createdBy),
      createdDate: new FormControl(recipientRawValue.createdDate),
      lastModifiedBy: new FormControl(recipientRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(recipientRawValue.lastModifiedDate),
    });
  }

  getRecipient(form: RecipientFormGroup): IRecipient | NewRecipient {
    return this.convertRecipientRawValueToRecipient(form.getRawValue() as RecipientFormRawValue | NewRecipientFormRawValue);
  }

  resetForm(form: RecipientFormGroup, recipient: RecipientFormGroupInput): void {
    const recipientRawValue = this.convertRecipientToRecipientRawValue({ ...this.getFormDefaults(), ...recipient });
    form.reset(
      {
        ...recipientRawValue,
        id: { value: recipientRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): RecipientFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertRecipientRawValueToRecipient(rawRecipient: RecipientFormRawValue | NewRecipientFormRawValue): IRecipient | NewRecipient {
    return {
      ...rawRecipient,
      createdDate: dayjs(rawRecipient.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawRecipient.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertRecipientToRecipientRawValue(
    recipient: IRecipient | (Partial<NewRecipient> & RecipientFormDefaults),
  ): RecipientFormRawValue | PartialWithRequiredKeyOf<NewRecipientFormRawValue> {
    return {
      ...recipient,
      createdDate: recipient.createdDate ? recipient.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: recipient.lastModifiedDate ? recipient.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
