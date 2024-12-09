import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IUserPreference, NewUserPreference } from '../user-preference.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IUserPreference for edit and NewUserPreferenceFormGroupInput for create.
 */
type UserPreferenceFormGroupInput = IUserPreference | PartialWithRequiredKeyOf<NewUserPreference>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IUserPreference | NewUserPreference> = Omit<T, 'createdDate'> & {
  createdDate?: string | null;
};

type UserPreferenceFormRawValue = FormValueOf<IUserPreference>;

type NewUserPreferenceFormRawValue = FormValueOf<NewUserPreference>;

type UserPreferenceFormDefaults = Pick<NewUserPreference, 'id' | 'createdDate'>;

type UserPreferenceFormGroupContent = {
  id: FormControl<UserPreferenceFormRawValue['id'] | NewUserPreference['id']>;
  app: FormControl<UserPreferenceFormRawValue['app']>;
  name: FormControl<UserPreferenceFormRawValue['name']>;
  setting: FormControl<UserPreferenceFormRawValue['setting']>;
  createdDate: FormControl<UserPreferenceFormRawValue['createdDate']>;
  user: FormControl<UserPreferenceFormRawValue['user']>;
};

export type UserPreferenceFormGroup = FormGroup<UserPreferenceFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class UserPreferenceFormService {
  createUserPreferenceFormGroup(userPreference: UserPreferenceFormGroupInput = { id: null }): UserPreferenceFormGroup {
    const userPreferenceRawValue = this.convertUserPreferenceToUserPreferenceRawValue({
      ...this.getFormDefaults(),
      ...userPreference,
    });
    return new FormGroup<UserPreferenceFormGroupContent>({
      id: new FormControl(
        { value: userPreferenceRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      app: new FormControl(userPreferenceRawValue.app),
      name: new FormControl(userPreferenceRawValue.name, {
        validators: [Validators.maxLength(50)],
      }),
      setting: new FormControl(userPreferenceRawValue.setting, {
        validators: [Validators.maxLength(100)],
      }),
      createdDate: new FormControl(userPreferenceRawValue.createdDate),
      user: new FormControl(userPreferenceRawValue.user),
    });
  }

  getUserPreference(form: UserPreferenceFormGroup): IUserPreference | NewUserPreference {
    return this.convertUserPreferenceRawValueToUserPreference(
      form.getRawValue() as UserPreferenceFormRawValue | NewUserPreferenceFormRawValue,
    );
  }

  resetForm(form: UserPreferenceFormGroup, userPreference: UserPreferenceFormGroupInput): void {
    const userPreferenceRawValue = this.convertUserPreferenceToUserPreferenceRawValue({ ...this.getFormDefaults(), ...userPreference });
    form.reset(
      {
        ...userPreferenceRawValue,
        id: { value: userPreferenceRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): UserPreferenceFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
    };
  }

  private convertUserPreferenceRawValueToUserPreference(
    rawUserPreference: UserPreferenceFormRawValue | NewUserPreferenceFormRawValue,
  ): IUserPreference | NewUserPreference {
    return {
      ...rawUserPreference,
      createdDate: dayjs(rawUserPreference.createdDate, DATE_TIME_FORMAT),
    };
  }

  private convertUserPreferenceToUserPreferenceRawValue(
    userPreference: IUserPreference | (Partial<NewUserPreference> & UserPreferenceFormDefaults),
  ): UserPreferenceFormRawValue | PartialWithRequiredKeyOf<NewUserPreferenceFormRawValue> {
    return {
      ...userPreference,
      createdDate: userPreference.createdDate ? userPreference.createdDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
