import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IUserAccess, NewUserAccess } from '../user-access.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IUserAccess for edit and NewUserAccessFormGroupInput for create.
 */
type UserAccessFormGroupInput = IUserAccess | PartialWithRequiredKeyOf<NewUserAccess>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IUserAccess | NewUserAccess> = Omit<T, 'createdDate'> & {
  createdDate?: string | null;
};

type UserAccessFormRawValue = FormValueOf<IUserAccess>;

type NewUserAccessFormRawValue = FormValueOf<NewUserAccess>;

type UserAccessFormDefaults = Pick<NewUserAccess, 'id' | 'createdDate'>;

type UserAccessFormGroupContent = {
  id: FormControl<UserAccessFormRawValue['id'] | NewUserAccess['id']>;
  login: FormControl<UserAccessFormRawValue['login']>;
  ipAddress: FormControl<UserAccessFormRawValue['ipAddress']>;
  device: FormControl<UserAccessFormRawValue['device']>;
  createdDate: FormControl<UserAccessFormRawValue['createdDate']>;
};

export type UserAccessFormGroup = FormGroup<UserAccessFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class UserAccessFormService {
  createUserAccessFormGroup(userAccess: UserAccessFormGroupInput = { id: null }): UserAccessFormGroup {
    const userAccessRawValue = this.convertUserAccessToUserAccessRawValue({
      ...this.getFormDefaults(),
      ...userAccess,
    });
    return new FormGroup<UserAccessFormGroupContent>({
      id: new FormControl(
        { value: userAccessRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      login: new FormControl(userAccessRawValue.login, {
        validators: [Validators.maxLength(50)],
      }),
      ipAddress: new FormControl(userAccessRawValue.ipAddress, {
        validators: [Validators.maxLength(50)],
      }),
      device: new FormControl(userAccessRawValue.device),
      createdDate: new FormControl(userAccessRawValue.createdDate),
    });
  }

  getUserAccess(form: UserAccessFormGroup): IUserAccess | NewUserAccess {
    return this.convertUserAccessRawValueToUserAccess(form.getRawValue() as UserAccessFormRawValue | NewUserAccessFormRawValue);
  }

  resetForm(form: UserAccessFormGroup, userAccess: UserAccessFormGroupInput): void {
    const userAccessRawValue = this.convertUserAccessToUserAccessRawValue({ ...this.getFormDefaults(), ...userAccess });
    form.reset(
      {
        ...userAccessRawValue,
        id: { value: userAccessRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): UserAccessFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
    };
  }

  private convertUserAccessRawValueToUserAccess(
    rawUserAccess: UserAccessFormRawValue | NewUserAccessFormRawValue,
  ): IUserAccess | NewUserAccess {
    return {
      ...rawUserAccess,
      createdDate: dayjs(rawUserAccess.createdDate, DATE_TIME_FORMAT),
    };
  }

  private convertUserAccessToUserAccessRawValue(
    userAccess: IUserAccess | (Partial<NewUserAccess> & UserAccessFormDefaults),
  ): UserAccessFormRawValue | PartialWithRequiredKeyOf<NewUserAccessFormRawValue> {
    return {
      ...userAccess,
      createdDate: userAccess.createdDate ? userAccess.createdDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
