import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IFailedAttempt, NewFailedAttempt } from '../failed-attempt.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFailedAttempt for edit and NewFailedAttemptFormGroupInput for create.
 */
type FailedAttemptFormGroupInput = IFailedAttempt | PartialWithRequiredKeyOf<NewFailedAttempt>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IFailedAttempt | NewFailedAttempt> = Omit<T, 'createdDate'> & {
  createdDate?: string | null;
};

type FailedAttemptFormRawValue = FormValueOf<IFailedAttempt>;

type NewFailedAttemptFormRawValue = FormValueOf<NewFailedAttempt>;

type FailedAttemptFormDefaults = Pick<NewFailedAttempt, 'id' | 'isAfterLock' | 'createdDate'>;

type FailedAttemptFormGroupContent = {
  id: FormControl<FailedAttemptFormRawValue['id'] | NewFailedAttempt['id']>;
  login: FormControl<FailedAttemptFormRawValue['login']>;
  ipAddress: FormControl<FailedAttemptFormRawValue['ipAddress']>;
  isAfterLock: FormControl<FailedAttemptFormRawValue['isAfterLock']>;
  app: FormControl<FailedAttemptFormRawValue['app']>;
  action: FormControl<FailedAttemptFormRawValue['action']>;
  device: FormControl<FailedAttemptFormRawValue['device']>;
  createdDate: FormControl<FailedAttemptFormRawValue['createdDate']>;
  reason: FormControl<FailedAttemptFormRawValue['reason']>;
};

export type FailedAttemptFormGroup = FormGroup<FailedAttemptFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FailedAttemptFormService {
  createFailedAttemptFormGroup(failedAttempt: FailedAttemptFormGroupInput = { id: null }): FailedAttemptFormGroup {
    const failedAttemptRawValue = this.convertFailedAttemptToFailedAttemptRawValue({
      ...this.getFormDefaults(),
      ...failedAttempt,
    });
    return new FormGroup<FailedAttemptFormGroupContent>({
      id: new FormControl(
        { value: failedAttemptRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      login: new FormControl(failedAttemptRawValue.login, {
        validators: [Validators.maxLength(50)],
      }),
      ipAddress: new FormControl(failedAttemptRawValue.ipAddress, {
        validators: [Validators.maxLength(50)],
      }),
      isAfterLock: new FormControl(failedAttemptRawValue.isAfterLock),
      app: new FormControl(failedAttemptRawValue.app),
      action: new FormControl(failedAttemptRawValue.action),
      device: new FormControl(failedAttemptRawValue.device),
      createdDate: new FormControl(failedAttemptRawValue.createdDate),
      reason: new FormControl(failedAttemptRawValue.reason, {
        validators: [Validators.maxLength(100)],
      }),
    });
  }

  getFailedAttempt(form: FailedAttemptFormGroup): IFailedAttempt | NewFailedAttempt {
    return this.convertFailedAttemptRawValueToFailedAttempt(form.getRawValue() as FailedAttemptFormRawValue | NewFailedAttemptFormRawValue);
  }

  resetForm(form: FailedAttemptFormGroup, failedAttempt: FailedAttemptFormGroupInput): void {
    const failedAttemptRawValue = this.convertFailedAttemptToFailedAttemptRawValue({ ...this.getFormDefaults(), ...failedAttempt });
    form.reset(
      {
        ...failedAttemptRawValue,
        id: { value: failedAttemptRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): FailedAttemptFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      isAfterLock: false,
      createdDate: currentTime,
    };
  }

  private convertFailedAttemptRawValueToFailedAttempt(
    rawFailedAttempt: FailedAttemptFormRawValue | NewFailedAttemptFormRawValue,
  ): IFailedAttempt | NewFailedAttempt {
    return {
      ...rawFailedAttempt,
      createdDate: dayjs(rawFailedAttempt.createdDate, DATE_TIME_FORMAT),
    };
  }

  private convertFailedAttemptToFailedAttemptRawValue(
    failedAttempt: IFailedAttempt | (Partial<NewFailedAttempt> & FailedAttemptFormDefaults),
  ): FailedAttemptFormRawValue | PartialWithRequiredKeyOf<NewFailedAttemptFormRawValue> {
    return {
      ...failedAttempt,
      createdDate: failedAttempt.createdDate ? failedAttempt.createdDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
