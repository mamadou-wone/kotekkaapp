import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IUserAffiliation, NewUserAffiliation } from '../user-affiliation.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IUserAffiliation for edit and NewUserAffiliationFormGroupInput for create.
 */
type UserAffiliationFormGroupInput = IUserAffiliation | PartialWithRequiredKeyOf<NewUserAffiliation>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IUserAffiliation | NewUserAffiliation> = Omit<T, 'createdDate'> & {
  createdDate?: string | null;
};

type UserAffiliationFormRawValue = FormValueOf<IUserAffiliation>;

type NewUserAffiliationFormRawValue = FormValueOf<NewUserAffiliation>;

type UserAffiliationFormDefaults = Pick<NewUserAffiliation, 'id' | 'createdDate'>;

type UserAffiliationFormGroupContent = {
  id: FormControl<UserAffiliationFormRawValue['id'] | NewUserAffiliation['id']>;
  walletHolder: FormControl<UserAffiliationFormRawValue['walletHolder']>;
  affiliation: FormControl<UserAffiliationFormRawValue['affiliation']>;
  createdBy: FormControl<UserAffiliationFormRawValue['createdBy']>;
  createdDate: FormControl<UserAffiliationFormRawValue['createdDate']>;
};

export type UserAffiliationFormGroup = FormGroup<UserAffiliationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class UserAffiliationFormService {
  createUserAffiliationFormGroup(userAffiliation: UserAffiliationFormGroupInput = { id: null }): UserAffiliationFormGroup {
    const userAffiliationRawValue = this.convertUserAffiliationToUserAffiliationRawValue({
      ...this.getFormDefaults(),
      ...userAffiliation,
    });
    return new FormGroup<UserAffiliationFormGroupContent>({
      id: new FormControl(
        { value: userAffiliationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      walletHolder: new FormControl(userAffiliationRawValue.walletHolder, {
        validators: [Validators.required],
      }),
      affiliation: new FormControl(userAffiliationRawValue.affiliation, {
        validators: [Validators.required, Validators.minLength(3), Validators.maxLength(100)],
      }),
      createdBy: new FormControl(userAffiliationRawValue.createdBy),
      createdDate: new FormControl(userAffiliationRawValue.createdDate),
    });
  }

  getUserAffiliation(form: UserAffiliationFormGroup): IUserAffiliation | NewUserAffiliation {
    return this.convertUserAffiliationRawValueToUserAffiliation(
      form.getRawValue() as UserAffiliationFormRawValue | NewUserAffiliationFormRawValue,
    );
  }

  resetForm(form: UserAffiliationFormGroup, userAffiliation: UserAffiliationFormGroupInput): void {
    const userAffiliationRawValue = this.convertUserAffiliationToUserAffiliationRawValue({ ...this.getFormDefaults(), ...userAffiliation });
    form.reset(
      {
        ...userAffiliationRawValue,
        id: { value: userAffiliationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): UserAffiliationFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
    };
  }

  private convertUserAffiliationRawValueToUserAffiliation(
    rawUserAffiliation: UserAffiliationFormRawValue | NewUserAffiliationFormRawValue,
  ): IUserAffiliation | NewUserAffiliation {
    return {
      ...rawUserAffiliation,
      createdDate: dayjs(rawUserAffiliation.createdDate, DATE_TIME_FORMAT),
    };
  }

  private convertUserAffiliationToUserAffiliationRawValue(
    userAffiliation: IUserAffiliation | (Partial<NewUserAffiliation> & UserAffiliationFormDefaults),
  ): UserAffiliationFormRawValue | PartialWithRequiredKeyOf<NewUserAffiliationFormRawValue> {
    return {
      ...userAffiliation,
      createdDate: userAffiliation.createdDate ? userAffiliation.createdDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
