import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IBeneficiary, NewBeneficiary } from '../beneficiary.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IBeneficiary for edit and NewBeneficiaryFormGroupInput for create.
 */
type BeneficiaryFormGroupInput = IBeneficiary | PartialWithRequiredKeyOf<NewBeneficiary>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IBeneficiary | NewBeneficiary> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type BeneficiaryFormRawValue = FormValueOf<IBeneficiary>;

type NewBeneficiaryFormRawValue = FormValueOf<NewBeneficiary>;

type BeneficiaryFormDefaults = Pick<NewBeneficiary, 'id' | 'createdDate' | 'lastModifiedDate'>;

type BeneficiaryFormGroupContent = {
  id: FormControl<BeneficiaryFormRawValue['id'] | NewBeneficiary['id']>;
  uuid: FormControl<BeneficiaryFormRawValue['uuid']>;
  status: FormControl<BeneficiaryFormRawValue['status']>;
  firstName: FormControl<BeneficiaryFormRawValue['firstName']>;
  lastName: FormControl<BeneficiaryFormRawValue['lastName']>;
  cin: FormControl<BeneficiaryFormRawValue['cin']>;
  iban: FormControl<BeneficiaryFormRawValue['iban']>;
  phoneNumber: FormControl<BeneficiaryFormRawValue['phoneNumber']>;
  email: FormControl<BeneficiaryFormRawValue['email']>;
  walletHolder: FormControl<BeneficiaryFormRawValue['walletHolder']>;
  createdBy: FormControl<BeneficiaryFormRawValue['createdBy']>;
  createdDate: FormControl<BeneficiaryFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<BeneficiaryFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<BeneficiaryFormRawValue['lastModifiedDate']>;
};

export type BeneficiaryFormGroup = FormGroup<BeneficiaryFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class BeneficiaryFormService {
  createBeneficiaryFormGroup(beneficiary: BeneficiaryFormGroupInput = { id: null }): BeneficiaryFormGroup {
    const beneficiaryRawValue = this.convertBeneficiaryToBeneficiaryRawValue({
      ...this.getFormDefaults(),
      ...beneficiary,
    });
    return new FormGroup<BeneficiaryFormGroupContent>({
      id: new FormControl(
        { value: beneficiaryRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      uuid: new FormControl(beneficiaryRawValue.uuid, {
        validators: [Validators.required],
      }),
      status: new FormControl(beneficiaryRawValue.status),
      firstName: new FormControl(beneficiaryRawValue.firstName, {
        validators: [Validators.maxLength(50)],
      }),
      lastName: new FormControl(beneficiaryRawValue.lastName, {
        validators: [Validators.maxLength(50)],
      }),
      cin: new FormControl(beneficiaryRawValue.cin, {
        validators: [Validators.minLength(6), Validators.maxLength(12)],
      }),
      iban: new FormControl(beneficiaryRawValue.iban, {
        validators: [Validators.maxLength(32)],
      }),
      phoneNumber: new FormControl(beneficiaryRawValue.phoneNumber, {
        validators: [Validators.maxLength(13)],
      }),
      email: new FormControl(beneficiaryRawValue.email, {
        validators: [Validators.minLength(5), Validators.maxLength(254)],
      }),
      walletHolder: new FormControl(beneficiaryRawValue.walletHolder),
      createdBy: new FormControl(beneficiaryRawValue.createdBy),
      createdDate: new FormControl(beneficiaryRawValue.createdDate),
      lastModifiedBy: new FormControl(beneficiaryRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(beneficiaryRawValue.lastModifiedDate),
    });
  }

  getBeneficiary(form: BeneficiaryFormGroup): IBeneficiary | NewBeneficiary {
    return this.convertBeneficiaryRawValueToBeneficiary(form.getRawValue() as BeneficiaryFormRawValue | NewBeneficiaryFormRawValue);
  }

  resetForm(form: BeneficiaryFormGroup, beneficiary: BeneficiaryFormGroupInput): void {
    const beneficiaryRawValue = this.convertBeneficiaryToBeneficiaryRawValue({ ...this.getFormDefaults(), ...beneficiary });
    form.reset(
      {
        ...beneficiaryRawValue,
        id: { value: beneficiaryRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): BeneficiaryFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertBeneficiaryRawValueToBeneficiary(
    rawBeneficiary: BeneficiaryFormRawValue | NewBeneficiaryFormRawValue,
  ): IBeneficiary | NewBeneficiary {
    return {
      ...rawBeneficiary,
      createdDate: dayjs(rawBeneficiary.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawBeneficiary.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertBeneficiaryToBeneficiaryRawValue(
    beneficiary: IBeneficiary | (Partial<NewBeneficiary> & BeneficiaryFormDefaults),
  ): BeneficiaryFormRawValue | PartialWithRequiredKeyOf<NewBeneficiaryFormRawValue> {
    return {
      ...beneficiary,
      createdDate: beneficiary.createdDate ? beneficiary.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: beneficiary.lastModifiedDate ? beneficiary.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
