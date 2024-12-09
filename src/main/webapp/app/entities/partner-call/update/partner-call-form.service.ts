import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IPartnerCall, NewPartnerCall } from '../partner-call.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPartnerCall for edit and NewPartnerCallFormGroupInput for create.
 */
type PartnerCallFormGroupInput = IPartnerCall | PartialWithRequiredKeyOf<NewPartnerCall>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IPartnerCall | NewPartnerCall> = Omit<T, 'requestTime' | 'responseTime'> & {
  requestTime?: string | null;
  responseTime?: string | null;
};

type PartnerCallFormRawValue = FormValueOf<IPartnerCall>;

type NewPartnerCallFormRawValue = FormValueOf<NewPartnerCall>;

type PartnerCallFormDefaults = Pick<NewPartnerCall, 'id' | 'requestTime' | 'responseTime'>;

type PartnerCallFormGroupContent = {
  id: FormControl<PartnerCallFormRawValue['id'] | NewPartnerCall['id']>;
  partner: FormControl<PartnerCallFormRawValue['partner']>;
  api: FormControl<PartnerCallFormRawValue['api']>;
  method: FormControl<PartnerCallFormRawValue['method']>;
  requestHeaders: FormControl<PartnerCallFormRawValue['requestHeaders']>;
  requestBody: FormControl<PartnerCallFormRawValue['requestBody']>;
  requestTime: FormControl<PartnerCallFormRawValue['requestTime']>;
  responseStatusCode: FormControl<PartnerCallFormRawValue['responseStatusCode']>;
  responseHeaders: FormControl<PartnerCallFormRawValue['responseHeaders']>;
  responseBody: FormControl<PartnerCallFormRawValue['responseBody']>;
  responseTime: FormControl<PartnerCallFormRawValue['responseTime']>;
  correlationId: FormControl<PartnerCallFormRawValue['correlationId']>;
  queryParam: FormControl<PartnerCallFormRawValue['queryParam']>;
};

export type PartnerCallFormGroup = FormGroup<PartnerCallFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PartnerCallFormService {
  createPartnerCallFormGroup(partnerCall: PartnerCallFormGroupInput = { id: null }): PartnerCallFormGroup {
    const partnerCallRawValue = this.convertPartnerCallToPartnerCallRawValue({
      ...this.getFormDefaults(),
      ...partnerCall,
    });
    return new FormGroup<PartnerCallFormGroupContent>({
      id: new FormControl(
        { value: partnerCallRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      partner: new FormControl(partnerCallRawValue.partner, {
        validators: [Validators.required],
      }),
      api: new FormControl(partnerCallRawValue.api, {
        validators: [Validators.maxLength(255)],
      }),
      method: new FormControl(partnerCallRawValue.method),
      requestHeaders: new FormControl(partnerCallRawValue.requestHeaders),
      requestBody: new FormControl(partnerCallRawValue.requestBody),
      requestTime: new FormControl(partnerCallRawValue.requestTime),
      responseStatusCode: new FormControl(partnerCallRawValue.responseStatusCode, {
        validators: [Validators.max(999)],
      }),
      responseHeaders: new FormControl(partnerCallRawValue.responseHeaders),
      responseBody: new FormControl(partnerCallRawValue.responseBody),
      responseTime: new FormControl(partnerCallRawValue.responseTime),
      correlationId: new FormControl(partnerCallRawValue.correlationId, {
        validators: [Validators.maxLength(50)],
      }),
      queryParam: new FormControl(partnerCallRawValue.queryParam, {
        validators: [Validators.maxLength(255)],
      }),
    });
  }

  getPartnerCall(form: PartnerCallFormGroup): IPartnerCall | NewPartnerCall {
    return this.convertPartnerCallRawValueToPartnerCall(form.getRawValue() as PartnerCallFormRawValue | NewPartnerCallFormRawValue);
  }

  resetForm(form: PartnerCallFormGroup, partnerCall: PartnerCallFormGroupInput): void {
    const partnerCallRawValue = this.convertPartnerCallToPartnerCallRawValue({ ...this.getFormDefaults(), ...partnerCall });
    form.reset(
      {
        ...partnerCallRawValue,
        id: { value: partnerCallRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PartnerCallFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      requestTime: currentTime,
      responseTime: currentTime,
    };
  }

  private convertPartnerCallRawValueToPartnerCall(
    rawPartnerCall: PartnerCallFormRawValue | NewPartnerCallFormRawValue,
  ): IPartnerCall | NewPartnerCall {
    return {
      ...rawPartnerCall,
      requestTime: dayjs(rawPartnerCall.requestTime, DATE_TIME_FORMAT),
      responseTime: dayjs(rawPartnerCall.responseTime, DATE_TIME_FORMAT),
    };
  }

  private convertPartnerCallToPartnerCallRawValue(
    partnerCall: IPartnerCall | (Partial<NewPartnerCall> & PartnerCallFormDefaults),
  ): PartnerCallFormRawValue | PartialWithRequiredKeyOf<NewPartnerCallFormRawValue> {
    return {
      ...partnerCall,
      requestTime: partnerCall.requestTime ? partnerCall.requestTime.format(DATE_TIME_FORMAT) : undefined,
      responseTime: partnerCall.responseTime ? partnerCall.responseTime.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
