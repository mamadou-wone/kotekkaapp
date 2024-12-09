import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IWalletHolder, NewWalletHolder } from '../wallet-holder.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IWalletHolder for edit and NewWalletHolderFormGroupInput for create.
 */
type WalletHolderFormGroupInput = IWalletHolder | PartialWithRequiredKeyOf<NewWalletHolder>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IWalletHolder | NewWalletHolder> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type WalletHolderFormRawValue = FormValueOf<IWalletHolder>;

type NewWalletHolderFormRawValue = FormValueOf<NewWalletHolder>;

type WalletHolderFormDefaults = Pick<NewWalletHolder, 'id' | 'createdDate' | 'lastModifiedDate'>;

type WalletHolderFormGroupContent = {
  id: FormControl<WalletHolderFormRawValue['id'] | NewWalletHolder['id']>;
  uuid: FormControl<WalletHolderFormRawValue['uuid']>;
  type: FormControl<WalletHolderFormRawValue['type']>;
  status: FormControl<WalletHolderFormRawValue['status']>;
  phoneNumber: FormControl<WalletHolderFormRawValue['phoneNumber']>;
  network: FormControl<WalletHolderFormRawValue['network']>;
  tag: FormControl<WalletHolderFormRawValue['tag']>;
  firstName: FormControl<WalletHolderFormRawValue['firstName']>;
  lastName: FormControl<WalletHolderFormRawValue['lastName']>;
  address: FormControl<WalletHolderFormRawValue['address']>;
  city: FormControl<WalletHolderFormRawValue['city']>;
  country: FormControl<WalletHolderFormRawValue['country']>;
  postalCode: FormControl<WalletHolderFormRawValue['postalCode']>;
  onboarding: FormControl<WalletHolderFormRawValue['onboarding']>;
  externalId: FormControl<WalletHolderFormRawValue['externalId']>;
  email: FormControl<WalletHolderFormRawValue['email']>;
  dateOfBirth: FormControl<WalletHolderFormRawValue['dateOfBirth']>;
  sex: FormControl<WalletHolderFormRawValue['sex']>;
  createdBy: FormControl<WalletHolderFormRawValue['createdBy']>;
  createdDate: FormControl<WalletHolderFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<WalletHolderFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<WalletHolderFormRawValue['lastModifiedDate']>;
  loginStatus: FormControl<WalletHolderFormRawValue['loginStatus']>;
  user: FormControl<WalletHolderFormRawValue['user']>;
};

export type WalletHolderFormGroup = FormGroup<WalletHolderFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class WalletHolderFormService {
  createWalletHolderFormGroup(walletHolder: WalletHolderFormGroupInput = { id: null }): WalletHolderFormGroup {
    const walletHolderRawValue = this.convertWalletHolderToWalletHolderRawValue({
      ...this.getFormDefaults(),
      ...walletHolder,
    });
    return new FormGroup<WalletHolderFormGroupContent>({
      id: new FormControl(
        { value: walletHolderRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      uuid: new FormControl(walletHolderRawValue.uuid, {
        validators: [Validators.required],
      }),
      type: new FormControl(walletHolderRawValue.type),
      status: new FormControl(walletHolderRawValue.status),
      phoneNumber: new FormControl(walletHolderRawValue.phoneNumber, {
        validators: [Validators.maxLength(50)],
      }),
      network: new FormControl(walletHolderRawValue.network),
      tag: new FormControl(walletHolderRawValue.tag, {
        validators: [Validators.maxLength(50)],
      }),
      firstName: new FormControl(walletHolderRawValue.firstName, {
        validators: [Validators.maxLength(50)],
      }),
      lastName: new FormControl(walletHolderRawValue.lastName, {
        validators: [Validators.maxLength(50)],
      }),
      address: new FormControl(walletHolderRawValue.address, {
        validators: [Validators.maxLength(50)],
      }),
      city: new FormControl(walletHolderRawValue.city, {
        validators: [Validators.maxLength(50)],
      }),
      country: new FormControl(walletHolderRawValue.country, {
        validators: [Validators.maxLength(50)],
      }),
      postalCode: new FormControl(walletHolderRawValue.postalCode, {
        validators: [Validators.maxLength(50)],
      }),
      onboarding: new FormControl(walletHolderRawValue.onboarding),
      externalId: new FormControl(walletHolderRawValue.externalId, {
        validators: [Validators.maxLength(36)],
      }),
      email: new FormControl(walletHolderRawValue.email, {
        validators: [Validators.minLength(5), Validators.maxLength(254)],
      }),
      dateOfBirth: new FormControl(walletHolderRawValue.dateOfBirth),
      sex: new FormControl(walletHolderRawValue.sex),
      createdBy: new FormControl(walletHolderRawValue.createdBy),
      createdDate: new FormControl(walletHolderRawValue.createdDate),
      lastModifiedBy: new FormControl(walletHolderRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(walletHolderRawValue.lastModifiedDate),
      loginStatus: new FormControl(walletHolderRawValue.loginStatus),
      user: new FormControl(walletHolderRawValue.user),
    });
  }

  getWalletHolder(form: WalletHolderFormGroup): IWalletHolder | NewWalletHolder {
    return this.convertWalletHolderRawValueToWalletHolder(form.getRawValue() as WalletHolderFormRawValue | NewWalletHolderFormRawValue);
  }

  resetForm(form: WalletHolderFormGroup, walletHolder: WalletHolderFormGroupInput): void {
    const walletHolderRawValue = this.convertWalletHolderToWalletHolderRawValue({ ...this.getFormDefaults(), ...walletHolder });
    form.reset(
      {
        ...walletHolderRawValue,
        id: { value: walletHolderRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): WalletHolderFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertWalletHolderRawValueToWalletHolder(
    rawWalletHolder: WalletHolderFormRawValue | NewWalletHolderFormRawValue,
  ): IWalletHolder | NewWalletHolder {
    return {
      ...rawWalletHolder,
      createdDate: dayjs(rawWalletHolder.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawWalletHolder.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertWalletHolderToWalletHolderRawValue(
    walletHolder: IWalletHolder | (Partial<NewWalletHolder> & WalletHolderFormDefaults),
  ): WalletHolderFormRawValue | PartialWithRequiredKeyOf<NewWalletHolderFormRawValue> {
    return {
      ...walletHolder,
      createdDate: walletHolder.createdDate ? walletHolder.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: walletHolder.lastModifiedDate ? walletHolder.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
