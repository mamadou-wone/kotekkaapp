import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICacheInfo, NewCacheInfo } from '../cache-info.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICacheInfo for edit and NewCacheInfoFormGroupInput for create.
 */
type CacheInfoFormGroupInput = ICacheInfo | PartialWithRequiredKeyOf<NewCacheInfo>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ICacheInfo | NewCacheInfo> = Omit<T, 'createdDate'> & {
  createdDate?: string | null;
};

type CacheInfoFormRawValue = FormValueOf<ICacheInfo>;

type NewCacheInfoFormRawValue = FormValueOf<NewCacheInfo>;

type CacheInfoFormDefaults = Pick<NewCacheInfo, 'id' | 'createdDate'>;

type CacheInfoFormGroupContent = {
  id: FormControl<CacheInfoFormRawValue['id'] | NewCacheInfo['id']>;
  walletHolder: FormControl<CacheInfoFormRawValue['walletHolder']>;
  key: FormControl<CacheInfoFormRawValue['key']>;
  value: FormControl<CacheInfoFormRawValue['value']>;
  createdDate: FormControl<CacheInfoFormRawValue['createdDate']>;
};

export type CacheInfoFormGroup = FormGroup<CacheInfoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CacheInfoFormService {
  createCacheInfoFormGroup(cacheInfo: CacheInfoFormGroupInput = { id: null }): CacheInfoFormGroup {
    const cacheInfoRawValue = this.convertCacheInfoToCacheInfoRawValue({
      ...this.getFormDefaults(),
      ...cacheInfo,
    });
    return new FormGroup<CacheInfoFormGroupContent>({
      id: new FormControl(
        { value: cacheInfoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      walletHolder: new FormControl(cacheInfoRawValue.walletHolder),
      key: new FormControl(cacheInfoRawValue.key, {
        validators: [Validators.maxLength(150)],
      }),
      value: new FormControl(cacheInfoRawValue.value),
      createdDate: new FormControl(cacheInfoRawValue.createdDate),
    });
  }

  getCacheInfo(form: CacheInfoFormGroup): ICacheInfo | NewCacheInfo {
    return this.convertCacheInfoRawValueToCacheInfo(form.getRawValue() as CacheInfoFormRawValue | NewCacheInfoFormRawValue);
  }

  resetForm(form: CacheInfoFormGroup, cacheInfo: CacheInfoFormGroupInput): void {
    const cacheInfoRawValue = this.convertCacheInfoToCacheInfoRawValue({ ...this.getFormDefaults(), ...cacheInfo });
    form.reset(
      {
        ...cacheInfoRawValue,
        id: { value: cacheInfoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CacheInfoFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
    };
  }

  private convertCacheInfoRawValueToCacheInfo(rawCacheInfo: CacheInfoFormRawValue | NewCacheInfoFormRawValue): ICacheInfo | NewCacheInfo {
    return {
      ...rawCacheInfo,
      createdDate: dayjs(rawCacheInfo.createdDate, DATE_TIME_FORMAT),
    };
  }

  private convertCacheInfoToCacheInfoRawValue(
    cacheInfo: ICacheInfo | (Partial<NewCacheInfo> & CacheInfoFormDefaults),
  ): CacheInfoFormRawValue | PartialWithRequiredKeyOf<NewCacheInfoFormRawValue> {
    return {
      ...cacheInfo,
      createdDate: cacheInfo.createdDate ? cacheInfo.createdDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
