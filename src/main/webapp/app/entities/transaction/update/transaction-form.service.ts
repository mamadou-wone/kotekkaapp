import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ITransaction, NewTransaction } from '../transaction.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITransaction for edit and NewTransactionFormGroupInput for create.
 */
type TransactionFormGroupInput = ITransaction | PartialWithRequiredKeyOf<NewTransaction>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ITransaction | NewTransaction> = Omit<T, 'startTime' | 'endTime' | 'createdDate' | 'lastModifiedDate'> & {
  startTime?: string | null;
  endTime?: string | null;
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type TransactionFormRawValue = FormValueOf<ITransaction>;

type NewTransactionFormRawValue = FormValueOf<NewTransaction>;

type TransactionFormDefaults = Pick<NewTransaction, 'id' | 'startTime' | 'endTime' | 'createdDate' | 'lastModifiedDate'>;

type TransactionFormGroupContent = {
  id: FormControl<TransactionFormRawValue['id'] | NewTransaction['id']>;
  uuid: FormControl<TransactionFormRawValue['uuid']>;
  type: FormControl<TransactionFormRawValue['type']>;
  status: FormControl<TransactionFormRawValue['status']>;
  direction: FormControl<TransactionFormRawValue['direction']>;
  amount: FormControl<TransactionFormRawValue['amount']>;
  description: FormControl<TransactionFormRawValue['description']>;
  fee: FormControl<TransactionFormRawValue['fee']>;
  commission: FormControl<TransactionFormRawValue['commission']>;
  currency: FormControl<TransactionFormRawValue['currency']>;
  counterpartyType: FormControl<TransactionFormRawValue['counterpartyType']>;
  counterpartyId: FormControl<TransactionFormRawValue['counterpartyId']>;
  entryDate: FormControl<TransactionFormRawValue['entryDate']>;
  effectiveDate: FormControl<TransactionFormRawValue['effectiveDate']>;
  startTime: FormControl<TransactionFormRawValue['startTime']>;
  endTime: FormControl<TransactionFormRawValue['endTime']>;
  parent: FormControl<TransactionFormRawValue['parent']>;
  reference: FormControl<TransactionFormRawValue['reference']>;
  externalId: FormControl<TransactionFormRawValue['externalId']>;
  partnerToken: FormControl<TransactionFormRawValue['partnerToken']>;
  wallet: FormControl<TransactionFormRawValue['wallet']>;
  createdBy: FormControl<TransactionFormRawValue['createdBy']>;
  createdDate: FormControl<TransactionFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<TransactionFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<TransactionFormRawValue['lastModifiedDate']>;
};

export type TransactionFormGroup = FormGroup<TransactionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TransactionFormService {
  createTransactionFormGroup(transaction: TransactionFormGroupInput = { id: null }): TransactionFormGroup {
    const transactionRawValue = this.convertTransactionToTransactionRawValue({
      ...this.getFormDefaults(),
      ...transaction,
    });
    return new FormGroup<TransactionFormGroupContent>({
      id: new FormControl(
        { value: transactionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      uuid: new FormControl(transactionRawValue.uuid, {
        validators: [Validators.required],
      }),
      type: new FormControl(transactionRawValue.type),
      status: new FormControl(transactionRawValue.status),
      direction: new FormControl(transactionRawValue.direction),
      amount: new FormControl(transactionRawValue.amount),
      description: new FormControl(transactionRawValue.description, {
        validators: [Validators.maxLength(100)],
      }),
      fee: new FormControl(transactionRawValue.fee),
      commission: new FormControl(transactionRawValue.commission),
      currency: new FormControl(transactionRawValue.currency, {
        validators: [Validators.maxLength(3)],
      }),
      counterpartyType: new FormControl(transactionRawValue.counterpartyType),
      counterpartyId: new FormControl(transactionRawValue.counterpartyId, {
        validators: [Validators.maxLength(36)],
      }),
      entryDate: new FormControl(transactionRawValue.entryDate),
      effectiveDate: new FormControl(transactionRawValue.effectiveDate),
      startTime: new FormControl(transactionRawValue.startTime),
      endTime: new FormControl(transactionRawValue.endTime),
      parent: new FormControl(transactionRawValue.parent),
      reference: new FormControl(transactionRawValue.reference, {
        validators: [Validators.maxLength(255)],
      }),
      externalId: new FormControl(transactionRawValue.externalId, {
        validators: [Validators.maxLength(255)],
      }),
      partnerToken: new FormControl(transactionRawValue.partnerToken, {
        validators: [Validators.maxLength(255)],
      }),
      wallet: new FormControl(transactionRawValue.wallet),
      createdBy: new FormControl(transactionRawValue.createdBy),
      createdDate: new FormControl(transactionRawValue.createdDate),
      lastModifiedBy: new FormControl(transactionRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(transactionRawValue.lastModifiedDate),
    });
  }

  getTransaction(form: TransactionFormGroup): ITransaction | NewTransaction {
    return this.convertTransactionRawValueToTransaction(form.getRawValue() as TransactionFormRawValue | NewTransactionFormRawValue);
  }

  resetForm(form: TransactionFormGroup, transaction: TransactionFormGroupInput): void {
    const transactionRawValue = this.convertTransactionToTransactionRawValue({ ...this.getFormDefaults(), ...transaction });
    form.reset(
      {
        ...transactionRawValue,
        id: { value: transactionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TransactionFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      startTime: currentTime,
      endTime: currentTime,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertTransactionRawValueToTransaction(
    rawTransaction: TransactionFormRawValue | NewTransactionFormRawValue,
  ): ITransaction | NewTransaction {
    return {
      ...rawTransaction,
      startTime: dayjs(rawTransaction.startTime, DATE_TIME_FORMAT),
      endTime: dayjs(rawTransaction.endTime, DATE_TIME_FORMAT),
      createdDate: dayjs(rawTransaction.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawTransaction.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertTransactionToTransactionRawValue(
    transaction: ITransaction | (Partial<NewTransaction> & TransactionFormDefaults),
  ): TransactionFormRawValue | PartialWithRequiredKeyOf<NewTransactionFormRawValue> {
    return {
      ...transaction,
      startTime: transaction.startTime ? transaction.startTime.format(DATE_TIME_FORMAT) : undefined,
      endTime: transaction.endTime ? transaction.endTime.format(DATE_TIME_FORMAT) : undefined,
      createdDate: transaction.createdDate ? transaction.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: transaction.lastModifiedDate ? transaction.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
