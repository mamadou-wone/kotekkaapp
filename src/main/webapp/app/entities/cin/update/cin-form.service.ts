import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICin, NewCin } from '../cin.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICin for edit and NewCinFormGroupInput for create.
 */
type CinFormGroupInput = ICin | PartialWithRequiredKeyOf<NewCin>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ICin | NewCin> = Omit<T, 'createdDate'> & {
  createdDate?: string | null;
};

type CinFormRawValue = FormValueOf<ICin>;

type NewCinFormRawValue = FormValueOf<NewCin>;

type CinFormDefaults = Pick<NewCin, 'id' | 'createdDate'>;

type CinFormGroupContent = {
  id: FormControl<CinFormRawValue['id'] | NewCin['id']>;
  cinId: FormControl<CinFormRawValue['cinId']>;
  validityDate: FormControl<CinFormRawValue['validityDate']>;
  birthDate: FormControl<CinFormRawValue['birthDate']>;
  birthPlace: FormControl<CinFormRawValue['birthPlace']>;
  firstName: FormControl<CinFormRawValue['firstName']>;
  lastName: FormControl<CinFormRawValue['lastName']>;
  birthCity: FormControl<CinFormRawValue['birthCity']>;
  fatherName: FormControl<CinFormRawValue['fatherName']>;
  nationality: FormControl<CinFormRawValue['nationality']>;
  nationalityCode: FormControl<CinFormRawValue['nationalityCode']>;
  issuingCountry: FormControl<CinFormRawValue['issuingCountry']>;
  issuingCountryCode: FormControl<CinFormRawValue['issuingCountryCode']>;
  motherName: FormControl<CinFormRawValue['motherName']>;
  civilRegister: FormControl<CinFormRawValue['civilRegister']>;
  sex: FormControl<CinFormRawValue['sex']>;
  address: FormControl<CinFormRawValue['address']>;
  birthCityCode: FormControl<CinFormRawValue['birthCityCode']>;
  walletHolder: FormControl<CinFormRawValue['walletHolder']>;
  createdDate: FormControl<CinFormRawValue['createdDate']>;
};

export type CinFormGroup = FormGroup<CinFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CinFormService {
  createCinFormGroup(cin: CinFormGroupInput = { id: null }): CinFormGroup {
    const cinRawValue = this.convertCinToCinRawValue({
      ...this.getFormDefaults(),
      ...cin,
    });
    return new FormGroup<CinFormGroupContent>({
      id: new FormControl(
        { value: cinRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      cinId: new FormControl(cinRawValue.cinId, {
        validators: [Validators.maxLength(50)],
      }),
      validityDate: new FormControl(cinRawValue.validityDate),
      birthDate: new FormControl(cinRawValue.birthDate),
      birthPlace: new FormControl(cinRawValue.birthPlace, {
        validators: [Validators.maxLength(255)],
      }),
      firstName: new FormControl(cinRawValue.firstName, {
        validators: [Validators.maxLength(255)],
      }),
      lastName: new FormControl(cinRawValue.lastName, {
        validators: [Validators.maxLength(255)],
      }),
      birthCity: new FormControl(cinRawValue.birthCity, {
        validators: [Validators.maxLength(255)],
      }),
      fatherName: new FormControl(cinRawValue.fatherName, {
        validators: [Validators.maxLength(255)],
      }),
      nationality: new FormControl(cinRawValue.nationality, {
        validators: [Validators.maxLength(255)],
      }),
      nationalityCode: new FormControl(cinRawValue.nationalityCode, {
        validators: [Validators.maxLength(255)],
      }),
      issuingCountry: new FormControl(cinRawValue.issuingCountry, {
        validators: [Validators.maxLength(255)],
      }),
      issuingCountryCode: new FormControl(cinRawValue.issuingCountryCode, {
        validators: [Validators.maxLength(3)],
      }),
      motherName: new FormControl(cinRawValue.motherName, {
        validators: [Validators.maxLength(255)],
      }),
      civilRegister: new FormControl(cinRawValue.civilRegister, {
        validators: [Validators.maxLength(255)],
      }),
      sex: new FormControl(cinRawValue.sex, {
        validators: [Validators.maxLength(3)],
      }),
      address: new FormControl(cinRawValue.address, {
        validators: [Validators.maxLength(255)],
      }),
      birthCityCode: new FormControl(cinRawValue.birthCityCode, {
        validators: [Validators.maxLength(3)],
      }),
      walletHolder: new FormControl(cinRawValue.walletHolder),
      createdDate: new FormControl(cinRawValue.createdDate),
    });
  }

  getCin(form: CinFormGroup): ICin | NewCin {
    return this.convertCinRawValueToCin(form.getRawValue() as CinFormRawValue | NewCinFormRawValue);
  }

  resetForm(form: CinFormGroup, cin: CinFormGroupInput): void {
    const cinRawValue = this.convertCinToCinRawValue({ ...this.getFormDefaults(), ...cin });
    form.reset(
      {
        ...cinRawValue,
        id: { value: cinRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CinFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
    };
  }

  private convertCinRawValueToCin(rawCin: CinFormRawValue | NewCinFormRawValue): ICin | NewCin {
    return {
      ...rawCin,
      createdDate: dayjs(rawCin.createdDate, DATE_TIME_FORMAT),
    };
  }

  private convertCinToCinRawValue(
    cin: ICin | (Partial<NewCin> & CinFormDefaults),
  ): CinFormRawValue | PartialWithRequiredKeyOf<NewCinFormRawValue> {
    return {
      ...cin,
      createdDate: cin.createdDate ? cin.createdDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
