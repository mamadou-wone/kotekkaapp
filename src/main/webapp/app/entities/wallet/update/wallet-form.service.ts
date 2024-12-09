import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IWallet, NewWallet } from '../wallet.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IWallet for edit and NewWalletFormGroupInput for create.
 */
type WalletFormGroupInput = IWallet | PartialWithRequiredKeyOf<NewWallet>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IWallet | NewWallet> = Omit<T, 'balancesAsOf' | 'createdDate' | 'lastModifiedDate'> & {
  balancesAsOf?: string | null;
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type WalletFormRawValue = FormValueOf<IWallet>;

type NewWalletFormRawValue = FormValueOf<NewWallet>;

type WalletFormDefaults = Pick<NewWallet, 'id' | 'balancesAsOf' | 'createdDate' | 'lastModifiedDate'>;

type WalletFormGroupContent = {
  id: FormControl<WalletFormRawValue['id'] | NewWallet['id']>;
  uuid: FormControl<WalletFormRawValue['uuid']>;
  type: FormControl<WalletFormRawValue['type']>;
  status: FormControl<WalletFormRawValue['status']>;
  level: FormControl<WalletFormRawValue['level']>;
  iban: FormControl<WalletFormRawValue['iban']>;
  currency: FormControl<WalletFormRawValue['currency']>;
  balance: FormControl<WalletFormRawValue['balance']>;
  balancesAsOf: FormControl<WalletFormRawValue['balancesAsOf']>;
  externalId: FormControl<WalletFormRawValue['externalId']>;
  walletHolder: FormControl<WalletFormRawValue['walletHolder']>;
  createdBy: FormControl<WalletFormRawValue['createdBy']>;
  createdDate: FormControl<WalletFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<WalletFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<WalletFormRawValue['lastModifiedDate']>;
};

export type WalletFormGroup = FormGroup<WalletFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class WalletFormService {
  createWalletFormGroup(wallet: WalletFormGroupInput = { id: null }): WalletFormGroup {
    const walletRawValue = this.convertWalletToWalletRawValue({
      ...this.getFormDefaults(),
      ...wallet,
    });
    return new FormGroup<WalletFormGroupContent>({
      id: new FormControl(
        { value: walletRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      uuid: new FormControl(walletRawValue.uuid, {
        validators: [Validators.required],
      }),
      type: new FormControl(walletRawValue.type),
      status: new FormControl(walletRawValue.status),
      level: new FormControl(walletRawValue.level),
      iban: new FormControl(walletRawValue.iban, {
        validators: [Validators.maxLength(32)],
      }),
      currency: new FormControl(walletRawValue.currency, {
        validators: [Validators.maxLength(3)],
      }),
      balance: new FormControl(walletRawValue.balance),
      balancesAsOf: new FormControl(walletRawValue.balancesAsOf),
      externalId: new FormControl(walletRawValue.externalId, {
        validators: [Validators.maxLength(36)],
      }),
      walletHolder: new FormControl(walletRawValue.walletHolder),
      createdBy: new FormControl(walletRawValue.createdBy),
      createdDate: new FormControl(walletRawValue.createdDate),
      lastModifiedBy: new FormControl(walletRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(walletRawValue.lastModifiedDate),
    });
  }

  getWallet(form: WalletFormGroup): IWallet | NewWallet {
    return this.convertWalletRawValueToWallet(form.getRawValue() as WalletFormRawValue | NewWalletFormRawValue);
  }

  resetForm(form: WalletFormGroup, wallet: WalletFormGroupInput): void {
    const walletRawValue = this.convertWalletToWalletRawValue({ ...this.getFormDefaults(), ...wallet });
    form.reset(
      {
        ...walletRawValue,
        id: { value: walletRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): WalletFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      balancesAsOf: currentTime,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertWalletRawValueToWallet(rawWallet: WalletFormRawValue | NewWalletFormRawValue): IWallet | NewWallet {
    return {
      ...rawWallet,
      balancesAsOf: dayjs(rawWallet.balancesAsOf, DATE_TIME_FORMAT),
      createdDate: dayjs(rawWallet.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawWallet.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertWalletToWalletRawValue(
    wallet: IWallet | (Partial<NewWallet> & WalletFormDefaults),
  ): WalletFormRawValue | PartialWithRequiredKeyOf<NewWalletFormRawValue> {
    return {
      ...wallet,
      balancesAsOf: wallet.balancesAsOf ? wallet.balancesAsOf.format(DATE_TIME_FORMAT) : undefined,
      createdDate: wallet.createdDate ? wallet.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: wallet.lastModifiedDate ? wallet.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
