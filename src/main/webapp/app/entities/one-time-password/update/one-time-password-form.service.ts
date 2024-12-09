import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IOneTimePassword, NewOneTimePassword } from '../one-time-password.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOneTimePassword for edit and NewOneTimePasswordFormGroupInput for create.
 */
type OneTimePasswordFormGroupInput = IOneTimePassword | PartialWithRequiredKeyOf<NewOneTimePassword>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IOneTimePassword | NewOneTimePassword> = Omit<T, 'expiry' | 'createdDate'> & {
  expiry?: string | null;
  createdDate?: string | null;
};

type OneTimePasswordFormRawValue = FormValueOf<IOneTimePassword>;

type NewOneTimePasswordFormRawValue = FormValueOf<NewOneTimePassword>;

type OneTimePasswordFormDefaults = Pick<NewOneTimePassword, 'id' | 'expiry' | 'createdDate'>;

type OneTimePasswordFormGroupContent = {
  id: FormControl<OneTimePasswordFormRawValue['id'] | NewOneTimePassword['id']>;
  uuid: FormControl<OneTimePasswordFormRawValue['uuid']>;
  user: FormControl<OneTimePasswordFormRawValue['user']>;
  code: FormControl<OneTimePasswordFormRawValue['code']>;
  status: FormControl<OneTimePasswordFormRawValue['status']>;
  expiry: FormControl<OneTimePasswordFormRawValue['expiry']>;
  createdDate: FormControl<OneTimePasswordFormRawValue['createdDate']>;
};

export type OneTimePasswordFormGroup = FormGroup<OneTimePasswordFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class OneTimePasswordFormService {
  createOneTimePasswordFormGroup(oneTimePassword: OneTimePasswordFormGroupInput = { id: null }): OneTimePasswordFormGroup {
    const oneTimePasswordRawValue = this.convertOneTimePasswordToOneTimePasswordRawValue({
      ...this.getFormDefaults(),
      ...oneTimePassword,
    });
    return new FormGroup<OneTimePasswordFormGroupContent>({
      id: new FormControl(
        { value: oneTimePasswordRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      uuid: new FormControl(oneTimePasswordRawValue.uuid, {
        validators: [Validators.required],
      }),
      user: new FormControl(oneTimePasswordRawValue.user, {
        validators: [Validators.maxLength(50)],
      }),
      code: new FormControl(oneTimePasswordRawValue.code, {
        validators: [Validators.maxLength(12)],
      }),
      status: new FormControl(oneTimePasswordRawValue.status),
      expiry: new FormControl(oneTimePasswordRawValue.expiry),
      createdDate: new FormControl(oneTimePasswordRawValue.createdDate),
    });
  }

  getOneTimePassword(form: OneTimePasswordFormGroup): IOneTimePassword | NewOneTimePassword {
    return this.convertOneTimePasswordRawValueToOneTimePassword(
      form.getRawValue() as OneTimePasswordFormRawValue | NewOneTimePasswordFormRawValue,
    );
  }

  resetForm(form: OneTimePasswordFormGroup, oneTimePassword: OneTimePasswordFormGroupInput): void {
    const oneTimePasswordRawValue = this.convertOneTimePasswordToOneTimePasswordRawValue({ ...this.getFormDefaults(), ...oneTimePassword });
    form.reset(
      {
        ...oneTimePasswordRawValue,
        id: { value: oneTimePasswordRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): OneTimePasswordFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      expiry: currentTime,
      createdDate: currentTime,
    };
  }

  private convertOneTimePasswordRawValueToOneTimePassword(
    rawOneTimePassword: OneTimePasswordFormRawValue | NewOneTimePasswordFormRawValue,
  ): IOneTimePassword | NewOneTimePassword {
    return {
      ...rawOneTimePassword,
      expiry: dayjs(rawOneTimePassword.expiry, DATE_TIME_FORMAT),
      createdDate: dayjs(rawOneTimePassword.createdDate, DATE_TIME_FORMAT),
    };
  }

  private convertOneTimePasswordToOneTimePasswordRawValue(
    oneTimePassword: IOneTimePassword | (Partial<NewOneTimePassword> & OneTimePasswordFormDefaults),
  ): OneTimePasswordFormRawValue | PartialWithRequiredKeyOf<NewOneTimePasswordFormRawValue> {
    return {
      ...oneTimePassword,
      expiry: oneTimePassword.expiry ? oneTimePassword.expiry.format(DATE_TIME_FORMAT) : undefined,
      createdDate: oneTimePassword.createdDate ? oneTimePassword.createdDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
