import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IReferalInfo, NewReferalInfo } from '../referal-info.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IReferalInfo for edit and NewReferalInfoFormGroupInput for create.
 */
type ReferalInfoFormGroupInput = IReferalInfo | PartialWithRequiredKeyOf<NewReferalInfo>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IReferalInfo | NewReferalInfo> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type ReferalInfoFormRawValue = FormValueOf<IReferalInfo>;

type NewReferalInfoFormRawValue = FormValueOf<NewReferalInfo>;

type ReferalInfoFormDefaults = Pick<NewReferalInfo, 'id' | 'createdDate' | 'lastModifiedDate'>;

type ReferalInfoFormGroupContent = {
  id: FormControl<ReferalInfoFormRawValue['id'] | NewReferalInfo['id']>;
  uuid: FormControl<ReferalInfoFormRawValue['uuid']>;
  referalCode: FormControl<ReferalInfoFormRawValue['referalCode']>;
  walletHolder: FormControl<ReferalInfoFormRawValue['walletHolder']>;
  refered: FormControl<ReferalInfoFormRawValue['refered']>;
  status: FormControl<ReferalInfoFormRawValue['status']>;
  createdBy: FormControl<ReferalInfoFormRawValue['createdBy']>;
  createdDate: FormControl<ReferalInfoFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<ReferalInfoFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<ReferalInfoFormRawValue['lastModifiedDate']>;
};

export type ReferalInfoFormGroup = FormGroup<ReferalInfoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ReferalInfoFormService {
  createReferalInfoFormGroup(referalInfo: ReferalInfoFormGroupInput = { id: null }): ReferalInfoFormGroup {
    const referalInfoRawValue = this.convertReferalInfoToReferalInfoRawValue({
      ...this.getFormDefaults(),
      ...referalInfo,
    });
    return new FormGroup<ReferalInfoFormGroupContent>({
      id: new FormControl(
        { value: referalInfoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      uuid: new FormControl(referalInfoRawValue.uuid, {
        validators: [Validators.required],
      }),
      referalCode: new FormControl(referalInfoRawValue.referalCode, {
        validators: [Validators.maxLength(36)],
      }),
      walletHolder: new FormControl(referalInfoRawValue.walletHolder),
      refered: new FormControl(referalInfoRawValue.refered),
      status: new FormControl(referalInfoRawValue.status),
      createdBy: new FormControl(referalInfoRawValue.createdBy),
      createdDate: new FormControl(referalInfoRawValue.createdDate),
      lastModifiedBy: new FormControl(referalInfoRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(referalInfoRawValue.lastModifiedDate),
    });
  }

  getReferalInfo(form: ReferalInfoFormGroup): IReferalInfo | NewReferalInfo {
    return this.convertReferalInfoRawValueToReferalInfo(form.getRawValue() as ReferalInfoFormRawValue | NewReferalInfoFormRawValue);
  }

  resetForm(form: ReferalInfoFormGroup, referalInfo: ReferalInfoFormGroupInput): void {
    const referalInfoRawValue = this.convertReferalInfoToReferalInfoRawValue({ ...this.getFormDefaults(), ...referalInfo });
    form.reset(
      {
        ...referalInfoRawValue,
        id: { value: referalInfoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ReferalInfoFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertReferalInfoRawValueToReferalInfo(
    rawReferalInfo: ReferalInfoFormRawValue | NewReferalInfoFormRawValue,
  ): IReferalInfo | NewReferalInfo {
    return {
      ...rawReferalInfo,
      createdDate: dayjs(rawReferalInfo.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawReferalInfo.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertReferalInfoToReferalInfoRawValue(
    referalInfo: IReferalInfo | (Partial<NewReferalInfo> & ReferalInfoFormDefaults),
  ): ReferalInfoFormRawValue | PartialWithRequiredKeyOf<NewReferalInfoFormRawValue> {
    return {
      ...referalInfo,
      createdDate: referalInfo.createdDate ? referalInfo.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: referalInfo.lastModifiedDate ? referalInfo.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
