import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IDevice, NewDevice } from '../device.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDevice for edit and NewDeviceFormGroupInput for create.
 */
type DeviceFormGroupInput = IDevice | PartialWithRequiredKeyOf<NewDevice>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IDevice | NewDevice> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type DeviceFormRawValue = FormValueOf<IDevice>;

type NewDeviceFormRawValue = FormValueOf<NewDevice>;

type DeviceFormDefaults = Pick<NewDevice, 'id' | 'inactive' | 'createdDate' | 'lastModifiedDate'>;

type DeviceFormGroupContent = {
  id: FormControl<DeviceFormRawValue['id'] | NewDevice['id']>;
  uuid: FormControl<DeviceFormRawValue['uuid']>;
  deviceUuid: FormControl<DeviceFormRawValue['deviceUuid']>;
  type: FormControl<DeviceFormRawValue['type']>;
  manufacturer: FormControl<DeviceFormRawValue['manufacturer']>;
  model: FormControl<DeviceFormRawValue['model']>;
  os: FormControl<DeviceFormRawValue['os']>;
  appVersion: FormControl<DeviceFormRawValue['appVersion']>;
  inactive: FormControl<DeviceFormRawValue['inactive']>;
  createdBy: FormControl<DeviceFormRawValue['createdBy']>;
  createdDate: FormControl<DeviceFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<DeviceFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<DeviceFormRawValue['lastModifiedDate']>;
};

export type DeviceFormGroup = FormGroup<DeviceFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DeviceFormService {
  createDeviceFormGroup(device: DeviceFormGroupInput = { id: null }): DeviceFormGroup {
    const deviceRawValue = this.convertDeviceToDeviceRawValue({
      ...this.getFormDefaults(),
      ...device,
    });
    return new FormGroup<DeviceFormGroupContent>({
      id: new FormControl(
        { value: deviceRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      uuid: new FormControl(deviceRawValue.uuid, {
        validators: [Validators.required],
      }),
      deviceUuid: new FormControl(deviceRawValue.deviceUuid, {
        validators: [Validators.maxLength(36)],
      }),
      type: new FormControl(deviceRawValue.type, {
        validators: [Validators.maxLength(100)],
      }),
      manufacturer: new FormControl(deviceRawValue.manufacturer, {
        validators: [Validators.maxLength(100)],
      }),
      model: new FormControl(deviceRawValue.model, {
        validators: [Validators.maxLength(100)],
      }),
      os: new FormControl(deviceRawValue.os, {
        validators: [Validators.maxLength(100)],
      }),
      appVersion: new FormControl(deviceRawValue.appVersion, {
        validators: [Validators.maxLength(100)],
      }),
      inactive: new FormControl(deviceRawValue.inactive),
      createdBy: new FormControl(deviceRawValue.createdBy),
      createdDate: new FormControl(deviceRawValue.createdDate),
      lastModifiedBy: new FormControl(deviceRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(deviceRawValue.lastModifiedDate),
    });
  }

  getDevice(form: DeviceFormGroup): IDevice | NewDevice {
    return this.convertDeviceRawValueToDevice(form.getRawValue() as DeviceFormRawValue | NewDeviceFormRawValue);
  }

  resetForm(form: DeviceFormGroup, device: DeviceFormGroupInput): void {
    const deviceRawValue = this.convertDeviceToDeviceRawValue({ ...this.getFormDefaults(), ...device });
    form.reset(
      {
        ...deviceRawValue,
        id: { value: deviceRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): DeviceFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      inactive: false,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertDeviceRawValueToDevice(rawDevice: DeviceFormRawValue | NewDeviceFormRawValue): IDevice | NewDevice {
    return {
      ...rawDevice,
      createdDate: dayjs(rawDevice.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawDevice.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertDeviceToDeviceRawValue(
    device: IDevice | (Partial<NewDevice> & DeviceFormDefaults),
  ): DeviceFormRawValue | PartialWithRequiredKeyOf<NewDeviceFormRawValue> {
    return {
      ...device,
      createdDate: device.createdDate ? device.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: device.lastModifiedDate ? device.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
