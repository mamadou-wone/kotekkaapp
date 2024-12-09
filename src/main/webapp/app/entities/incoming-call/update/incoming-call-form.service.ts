import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IIncomingCall, NewIncomingCall } from '../incoming-call.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IIncomingCall for edit and NewIncomingCallFormGroupInput for create.
 */
type IncomingCallFormGroupInput = IIncomingCall | PartialWithRequiredKeyOf<NewIncomingCall>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IIncomingCall | NewIncomingCall> = Omit<T, 'createdDate' | 'responseTime'> & {
  createdDate?: string | null;
  responseTime?: string | null;
};

type IncomingCallFormRawValue = FormValueOf<IIncomingCall>;

type NewIncomingCallFormRawValue = FormValueOf<NewIncomingCall>;

type IncomingCallFormDefaults = Pick<NewIncomingCall, 'id' | 'createdDate' | 'responseTime'>;

type IncomingCallFormGroupContent = {
  id: FormControl<IncomingCallFormRawValue['id'] | NewIncomingCall['id']>;
  partner: FormControl<IncomingCallFormRawValue['partner']>;
  api: FormControl<IncomingCallFormRawValue['api']>;
  method: FormControl<IncomingCallFormRawValue['method']>;
  requestHeaders: FormControl<IncomingCallFormRawValue['requestHeaders']>;
  requestBody: FormControl<IncomingCallFormRawValue['requestBody']>;
  createdDate: FormControl<IncomingCallFormRawValue['createdDate']>;
  responseStatusCode: FormControl<IncomingCallFormRawValue['responseStatusCode']>;
  responseTime: FormControl<IncomingCallFormRawValue['responseTime']>;
};

export type IncomingCallFormGroup = FormGroup<IncomingCallFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class IncomingCallFormService {
  createIncomingCallFormGroup(incomingCall: IncomingCallFormGroupInput = { id: null }): IncomingCallFormGroup {
    const incomingCallRawValue = this.convertIncomingCallToIncomingCallRawValue({
      ...this.getFormDefaults(),
      ...incomingCall,
    });
    return new FormGroup<IncomingCallFormGroupContent>({
      id: new FormControl(
        { value: incomingCallRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      partner: new FormControl(incomingCallRawValue.partner),
      api: new FormControl(incomingCallRawValue.api, {
        validators: [Validators.maxLength(255)],
      }),
      method: new FormControl(incomingCallRawValue.method),
      requestHeaders: new FormControl(incomingCallRawValue.requestHeaders),
      requestBody: new FormControl(incomingCallRawValue.requestBody),
      createdDate: new FormControl(incomingCallRawValue.createdDate),
      responseStatusCode: new FormControl(incomingCallRawValue.responseStatusCode, {
        validators: [Validators.max(999)],
      }),
      responseTime: new FormControl(incomingCallRawValue.responseTime),
    });
  }

  getIncomingCall(form: IncomingCallFormGroup): IIncomingCall | NewIncomingCall {
    return this.convertIncomingCallRawValueToIncomingCall(form.getRawValue() as IncomingCallFormRawValue | NewIncomingCallFormRawValue);
  }

  resetForm(form: IncomingCallFormGroup, incomingCall: IncomingCallFormGroupInput): void {
    const incomingCallRawValue = this.convertIncomingCallToIncomingCallRawValue({ ...this.getFormDefaults(), ...incomingCall });
    form.reset(
      {
        ...incomingCallRawValue,
        id: { value: incomingCallRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): IncomingCallFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
      responseTime: currentTime,
    };
  }

  private convertIncomingCallRawValueToIncomingCall(
    rawIncomingCall: IncomingCallFormRawValue | NewIncomingCallFormRawValue,
  ): IIncomingCall | NewIncomingCall {
    return {
      ...rawIncomingCall,
      createdDate: dayjs(rawIncomingCall.createdDate, DATE_TIME_FORMAT),
      responseTime: dayjs(rawIncomingCall.responseTime, DATE_TIME_FORMAT),
    };
  }

  private convertIncomingCallToIncomingCallRawValue(
    incomingCall: IIncomingCall | (Partial<NewIncomingCall> & IncomingCallFormDefaults),
  ): IncomingCallFormRawValue | PartialWithRequiredKeyOf<NewIncomingCallFormRawValue> {
    return {
      ...incomingCall,
      createdDate: incomingCall.createdDate ? incomingCall.createdDate.format(DATE_TIME_FORMAT) : undefined,
      responseTime: incomingCall.responseTime ? incomingCall.responseTime.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
