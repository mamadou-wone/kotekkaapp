import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IMoneyRequest, NewMoneyRequest } from '../money-request.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMoneyRequest for edit and NewMoneyRequestFormGroupInput for create.
 */
type MoneyRequestFormGroupInput = IMoneyRequest | PartialWithRequiredKeyOf<NewMoneyRequest>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IMoneyRequest | NewMoneyRequest> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type MoneyRequestFormRawValue = FormValueOf<IMoneyRequest>;

type NewMoneyRequestFormRawValue = FormValueOf<NewMoneyRequest>;

type MoneyRequestFormDefaults = Pick<NewMoneyRequest, 'id' | 'createdDate' | 'lastModifiedDate'>;

type MoneyRequestFormGroupContent = {
  id: FormControl<MoneyRequestFormRawValue['id'] | NewMoneyRequest['id']>;
  uuid: FormControl<MoneyRequestFormRawValue['uuid']>;
  status: FormControl<MoneyRequestFormRawValue['status']>;
  otherHolder: FormControl<MoneyRequestFormRawValue['otherHolder']>;
  amount: FormControl<MoneyRequestFormRawValue['amount']>;
  description: FormControl<MoneyRequestFormRawValue['description']>;
  currency: FormControl<MoneyRequestFormRawValue['currency']>;
  walletHolder: FormControl<MoneyRequestFormRawValue['walletHolder']>;
  createdBy: FormControl<MoneyRequestFormRawValue['createdBy']>;
  createdDate: FormControl<MoneyRequestFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<MoneyRequestFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<MoneyRequestFormRawValue['lastModifiedDate']>;
};

export type MoneyRequestFormGroup = FormGroup<MoneyRequestFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MoneyRequestFormService {
  createMoneyRequestFormGroup(moneyRequest: MoneyRequestFormGroupInput = { id: null }): MoneyRequestFormGroup {
    const moneyRequestRawValue = this.convertMoneyRequestToMoneyRequestRawValue({
      ...this.getFormDefaults(),
      ...moneyRequest,
    });
    return new FormGroup<MoneyRequestFormGroupContent>({
      id: new FormControl(
        { value: moneyRequestRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      uuid: new FormControl(moneyRequestRawValue.uuid, {
        validators: [Validators.required],
      }),
      status: new FormControl(moneyRequestRawValue.status),
      otherHolder: new FormControl(moneyRequestRawValue.otherHolder),
      amount: new FormControl(moneyRequestRawValue.amount),
      description: new FormControl(moneyRequestRawValue.description, {
        validators: [Validators.maxLength(100)],
      }),
      currency: new FormControl(moneyRequestRawValue.currency, {
        validators: [Validators.maxLength(3)],
      }),
      walletHolder: new FormControl(moneyRequestRawValue.walletHolder),
      createdBy: new FormControl(moneyRequestRawValue.createdBy),
      createdDate: new FormControl(moneyRequestRawValue.createdDate),
      lastModifiedBy: new FormControl(moneyRequestRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(moneyRequestRawValue.lastModifiedDate),
    });
  }

  getMoneyRequest(form: MoneyRequestFormGroup): IMoneyRequest | NewMoneyRequest {
    return this.convertMoneyRequestRawValueToMoneyRequest(form.getRawValue() as MoneyRequestFormRawValue | NewMoneyRequestFormRawValue);
  }

  resetForm(form: MoneyRequestFormGroup, moneyRequest: MoneyRequestFormGroupInput): void {
    const moneyRequestRawValue = this.convertMoneyRequestToMoneyRequestRawValue({ ...this.getFormDefaults(), ...moneyRequest });
    form.reset(
      {
        ...moneyRequestRawValue,
        id: { value: moneyRequestRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): MoneyRequestFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertMoneyRequestRawValueToMoneyRequest(
    rawMoneyRequest: MoneyRequestFormRawValue | NewMoneyRequestFormRawValue,
  ): IMoneyRequest | NewMoneyRequest {
    return {
      ...rawMoneyRequest,
      createdDate: dayjs(rawMoneyRequest.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawMoneyRequest.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertMoneyRequestToMoneyRequestRawValue(
    moneyRequest: IMoneyRequest | (Partial<NewMoneyRequest> & MoneyRequestFormDefaults),
  ): MoneyRequestFormRawValue | PartialWithRequiredKeyOf<NewMoneyRequestFormRawValue> {
    return {
      ...moneyRequest,
      createdDate: moneyRequest.createdDate ? moneyRequest.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: moneyRequest.lastModifiedDate ? moneyRequest.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
