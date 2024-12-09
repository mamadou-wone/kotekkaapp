import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IServiceClient, NewServiceClient } from '../service-client.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IServiceClient for edit and NewServiceClientFormGroupInput for create.
 */
type ServiceClientFormGroupInput = IServiceClient | PartialWithRequiredKeyOf<NewServiceClient>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IServiceClient | NewServiceClient> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type ServiceClientFormRawValue = FormValueOf<IServiceClient>;

type NewServiceClientFormRawValue = FormValueOf<NewServiceClient>;

type ServiceClientFormDefaults = Pick<NewServiceClient, 'id' | 'createdDate' | 'lastModifiedDate'>;

type ServiceClientFormGroupContent = {
  id: FormControl<ServiceClientFormRawValue['id'] | NewServiceClient['id']>;
  clientId: FormControl<ServiceClientFormRawValue['clientId']>;
  type: FormControl<ServiceClientFormRawValue['type']>;
  apiKey: FormControl<ServiceClientFormRawValue['apiKey']>;
  status: FormControl<ServiceClientFormRawValue['status']>;
  note: FormControl<ServiceClientFormRawValue['note']>;
  createdDate: FormControl<ServiceClientFormRawValue['createdDate']>;
  lastModifiedDate: FormControl<ServiceClientFormRawValue['lastModifiedDate']>;
};

export type ServiceClientFormGroup = FormGroup<ServiceClientFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ServiceClientFormService {
  createServiceClientFormGroup(serviceClient: ServiceClientFormGroupInput = { id: null }): ServiceClientFormGroup {
    const serviceClientRawValue = this.convertServiceClientToServiceClientRawValue({
      ...this.getFormDefaults(),
      ...serviceClient,
    });
    return new FormGroup<ServiceClientFormGroupContent>({
      id: new FormControl(
        { value: serviceClientRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      clientId: new FormControl(serviceClientRawValue.clientId),
      type: new FormControl(serviceClientRawValue.type),
      apiKey: new FormControl(serviceClientRawValue.apiKey),
      status: new FormControl(serviceClientRawValue.status),
      note: new FormControl(serviceClientRawValue.note, {
        validators: [Validators.maxLength(255)],
      }),
      createdDate: new FormControl(serviceClientRawValue.createdDate),
      lastModifiedDate: new FormControl(serviceClientRawValue.lastModifiedDate),
    });
  }

  getServiceClient(form: ServiceClientFormGroup): IServiceClient | NewServiceClient {
    return this.convertServiceClientRawValueToServiceClient(form.getRawValue() as ServiceClientFormRawValue | NewServiceClientFormRawValue);
  }

  resetForm(form: ServiceClientFormGroup, serviceClient: ServiceClientFormGroupInput): void {
    const serviceClientRawValue = this.convertServiceClientToServiceClientRawValue({ ...this.getFormDefaults(), ...serviceClient });
    form.reset(
      {
        ...serviceClientRawValue,
        id: { value: serviceClientRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ServiceClientFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertServiceClientRawValueToServiceClient(
    rawServiceClient: ServiceClientFormRawValue | NewServiceClientFormRawValue,
  ): IServiceClient | NewServiceClient {
    return {
      ...rawServiceClient,
      createdDate: dayjs(rawServiceClient.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawServiceClient.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertServiceClientToServiceClientRawValue(
    serviceClient: IServiceClient | (Partial<NewServiceClient> & ServiceClientFormDefaults),
  ): ServiceClientFormRawValue | PartialWithRequiredKeyOf<NewServiceClientFormRawValue> {
    return {
      ...serviceClient,
      createdDate: serviceClient.createdDate ? serviceClient.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: serviceClient.lastModifiedDate ? serviceClient.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
