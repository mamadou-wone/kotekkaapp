import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { INotification, NewNotification } from '../notification.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts INotification for edit and NewNotificationFormGroupInput for create.
 */
type NotificationFormGroupInput = INotification | PartialWithRequiredKeyOf<NewNotification>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends INotification | NewNotification> = Omit<T, 'createdDate'> & {
  createdDate?: string | null;
};

type NotificationFormRawValue = FormValueOf<INotification>;

type NewNotificationFormRawValue = FormValueOf<NewNotification>;

type NotificationFormDefaults = Pick<NewNotification, 'id' | 'createdDate'>;

type NotificationFormGroupContent = {
  id: FormControl<NotificationFormRawValue['id'] | NewNotification['id']>;
  uuid: FormControl<NotificationFormRawValue['uuid']>;
  walletHolder: FormControl<NotificationFormRawValue['walletHolder']>;
  heading: FormControl<NotificationFormRawValue['heading']>;
  status: FormControl<NotificationFormRawValue['status']>;
  content: FormControl<NotificationFormRawValue['content']>;
  data: FormControl<NotificationFormRawValue['data']>;
  language: FormControl<NotificationFormRawValue['language']>;
  createdDate: FormControl<NotificationFormRawValue['createdDate']>;
};

export type NotificationFormGroup = FormGroup<NotificationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class NotificationFormService {
  createNotificationFormGroup(notification: NotificationFormGroupInput = { id: null }): NotificationFormGroup {
    const notificationRawValue = this.convertNotificationToNotificationRawValue({
      ...this.getFormDefaults(),
      ...notification,
    });
    return new FormGroup<NotificationFormGroupContent>({
      id: new FormControl(
        { value: notificationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      uuid: new FormControl(notificationRawValue.uuid),
      walletHolder: new FormControl(notificationRawValue.walletHolder),
      heading: new FormControl(notificationRawValue.heading, {
        validators: [Validators.maxLength(100)],
      }),
      status: new FormControl(notificationRawValue.status),
      content: new FormControl(notificationRawValue.content, {
        validators: [Validators.maxLength(254)],
      }),
      data: new FormControl(notificationRawValue.data, {
        validators: [Validators.maxLength(254)],
      }),
      language: new FormControl(notificationRawValue.language, {
        validators: [Validators.maxLength(2)],
      }),
      createdDate: new FormControl(notificationRawValue.createdDate),
    });
  }

  getNotification(form: NotificationFormGroup): INotification | NewNotification {
    return this.convertNotificationRawValueToNotification(form.getRawValue() as NotificationFormRawValue | NewNotificationFormRawValue);
  }

  resetForm(form: NotificationFormGroup, notification: NotificationFormGroupInput): void {
    const notificationRawValue = this.convertNotificationToNotificationRawValue({ ...this.getFormDefaults(), ...notification });
    form.reset(
      {
        ...notificationRawValue,
        id: { value: notificationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): NotificationFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
    };
  }

  private convertNotificationRawValueToNotification(
    rawNotification: NotificationFormRawValue | NewNotificationFormRawValue,
  ): INotification | NewNotification {
    return {
      ...rawNotification,
      createdDate: dayjs(rawNotification.createdDate, DATE_TIME_FORMAT),
    };
  }

  private convertNotificationToNotificationRawValue(
    notification: INotification | (Partial<NewNotification> & NotificationFormDefaults),
  ): NotificationFormRawValue | PartialWithRequiredKeyOf<NewNotificationFormRawValue> {
    return {
      ...notification,
      createdDate: notification.createdDate ? notification.createdDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
