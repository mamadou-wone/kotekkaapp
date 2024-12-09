import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IFailedAttemptHistory, NewFailedAttemptHistory } from '../failed-attempt-history.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFailedAttemptHistory for edit and NewFailedAttemptHistoryFormGroupInput for create.
 */
type FailedAttemptHistoryFormGroupInput = IFailedAttemptHistory | PartialWithRequiredKeyOf<NewFailedAttemptHistory>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IFailedAttemptHistory | NewFailedAttemptHistory> = Omit<T, 'createdDate'> & {
  createdDate?: string | null;
};

type FailedAttemptHistoryFormRawValue = FormValueOf<IFailedAttemptHistory>;

type NewFailedAttemptHistoryFormRawValue = FormValueOf<NewFailedAttemptHistory>;

type FailedAttemptHistoryFormDefaults = Pick<NewFailedAttemptHistory, 'id' | 'isAfterLock' | 'createdDate'>;

type FailedAttemptHistoryFormGroupContent = {
  id: FormControl<FailedAttemptHistoryFormRawValue['id'] | NewFailedAttemptHistory['id']>;
  login: FormControl<FailedAttemptHistoryFormRawValue['login']>;
  ipAddress: FormControl<FailedAttemptHistoryFormRawValue['ipAddress']>;
  isAfterLock: FormControl<FailedAttemptHistoryFormRawValue['isAfterLock']>;
  app: FormControl<FailedAttemptHistoryFormRawValue['app']>;
  action: FormControl<FailedAttemptHistoryFormRawValue['action']>;
  device: FormControl<FailedAttemptHistoryFormRawValue['device']>;
  createdDate: FormControl<FailedAttemptHistoryFormRawValue['createdDate']>;
  reason: FormControl<FailedAttemptHistoryFormRawValue['reason']>;
};

export type FailedAttemptHistoryFormGroup = FormGroup<FailedAttemptHistoryFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FailedAttemptHistoryFormService {
  createFailedAttemptHistoryFormGroup(
    failedAttemptHistory: FailedAttemptHistoryFormGroupInput = { id: null },
  ): FailedAttemptHistoryFormGroup {
    const failedAttemptHistoryRawValue = this.convertFailedAttemptHistoryToFailedAttemptHistoryRawValue({
      ...this.getFormDefaults(),
      ...failedAttemptHistory,
    });
    return new FormGroup<FailedAttemptHistoryFormGroupContent>({
      id: new FormControl(
        { value: failedAttemptHistoryRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      login: new FormControl(failedAttemptHistoryRawValue.login, {
        validators: [Validators.maxLength(50)],
      }),
      ipAddress: new FormControl(failedAttemptHistoryRawValue.ipAddress, {
        validators: [Validators.maxLength(50)],
      }),
      isAfterLock: new FormControl(failedAttemptHistoryRawValue.isAfterLock),
      app: new FormControl(failedAttemptHistoryRawValue.app),
      action: new FormControl(failedAttemptHistoryRawValue.action),
      device: new FormControl(failedAttemptHistoryRawValue.device),
      createdDate: new FormControl(failedAttemptHistoryRawValue.createdDate),
      reason: new FormControl(failedAttemptHistoryRawValue.reason, {
        validators: [Validators.maxLength(100)],
      }),
    });
  }

  getFailedAttemptHistory(form: FailedAttemptHistoryFormGroup): IFailedAttemptHistory | NewFailedAttemptHistory {
    return this.convertFailedAttemptHistoryRawValueToFailedAttemptHistory(
      form.getRawValue() as FailedAttemptHistoryFormRawValue | NewFailedAttemptHistoryFormRawValue,
    );
  }

  resetForm(form: FailedAttemptHistoryFormGroup, failedAttemptHistory: FailedAttemptHistoryFormGroupInput): void {
    const failedAttemptHistoryRawValue = this.convertFailedAttemptHistoryToFailedAttemptHistoryRawValue({
      ...this.getFormDefaults(),
      ...failedAttemptHistory,
    });
    form.reset(
      {
        ...failedAttemptHistoryRawValue,
        id: { value: failedAttemptHistoryRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): FailedAttemptHistoryFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      isAfterLock: false,
      createdDate: currentTime,
    };
  }

  private convertFailedAttemptHistoryRawValueToFailedAttemptHistory(
    rawFailedAttemptHistory: FailedAttemptHistoryFormRawValue | NewFailedAttemptHistoryFormRawValue,
  ): IFailedAttemptHistory | NewFailedAttemptHistory {
    return {
      ...rawFailedAttemptHistory,
      createdDate: dayjs(rawFailedAttemptHistory.createdDate, DATE_TIME_FORMAT),
    };
  }

  private convertFailedAttemptHistoryToFailedAttemptHistoryRawValue(
    failedAttemptHistory: IFailedAttemptHistory | (Partial<NewFailedAttemptHistory> & FailedAttemptHistoryFormDefaults),
  ): FailedAttemptHistoryFormRawValue | PartialWithRequiredKeyOf<NewFailedAttemptHistoryFormRawValue> {
    return {
      ...failedAttemptHistory,
      createdDate: failedAttemptHistory.createdDate ? failedAttemptHistory.createdDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
