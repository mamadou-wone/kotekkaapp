import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IFeatureFlag, NewFeatureFlag } from '../feature-flag.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFeatureFlag for edit and NewFeatureFlagFormGroupInput for create.
 */
type FeatureFlagFormGroupInput = IFeatureFlag | PartialWithRequiredKeyOf<NewFeatureFlag>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IFeatureFlag | NewFeatureFlag> = Omit<T, 'createdDate' | 'updatedDate'> & {
  createdDate?: string | null;
  updatedDate?: string | null;
};

type FeatureFlagFormRawValue = FormValueOf<IFeatureFlag>;

type NewFeatureFlagFormRawValue = FormValueOf<NewFeatureFlag>;

type FeatureFlagFormDefaults = Pick<NewFeatureFlag, 'id' | 'enabled' | 'createdDate' | 'updatedDate'>;

type FeatureFlagFormGroupContent = {
  id: FormControl<FeatureFlagFormRawValue['id'] | NewFeatureFlag['id']>;
  name: FormControl<FeatureFlagFormRawValue['name']>;
  enabled: FormControl<FeatureFlagFormRawValue['enabled']>;
  description: FormControl<FeatureFlagFormRawValue['description']>;
  createdBy: FormControl<FeatureFlagFormRawValue['createdBy']>;
  createdDate: FormControl<FeatureFlagFormRawValue['createdDate']>;
  updatedBy: FormControl<FeatureFlagFormRawValue['updatedBy']>;
  updatedDate: FormControl<FeatureFlagFormRawValue['updatedDate']>;
};

export type FeatureFlagFormGroup = FormGroup<FeatureFlagFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FeatureFlagFormService {
  createFeatureFlagFormGroup(featureFlag: FeatureFlagFormGroupInput = { id: null }): FeatureFlagFormGroup {
    const featureFlagRawValue = this.convertFeatureFlagToFeatureFlagRawValue({
      ...this.getFormDefaults(),
      ...featureFlag,
    });
    return new FormGroup<FeatureFlagFormGroupContent>({
      id: new FormControl(
        { value: featureFlagRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(featureFlagRawValue.name),
      enabled: new FormControl(featureFlagRawValue.enabled),
      description: new FormControl(featureFlagRawValue.description),
      createdBy: new FormControl(featureFlagRawValue.createdBy),
      createdDate: new FormControl(featureFlagRawValue.createdDate),
      updatedBy: new FormControl(featureFlagRawValue.updatedBy),
      updatedDate: new FormControl(featureFlagRawValue.updatedDate),
    });
  }

  getFeatureFlag(form: FeatureFlagFormGroup): IFeatureFlag | NewFeatureFlag {
    return this.convertFeatureFlagRawValueToFeatureFlag(form.getRawValue() as FeatureFlagFormRawValue | NewFeatureFlagFormRawValue);
  }

  resetForm(form: FeatureFlagFormGroup, featureFlag: FeatureFlagFormGroupInput): void {
    const featureFlagRawValue = this.convertFeatureFlagToFeatureFlagRawValue({ ...this.getFormDefaults(), ...featureFlag });
    form.reset(
      {
        ...featureFlagRawValue,
        id: { value: featureFlagRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): FeatureFlagFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      enabled: false,
      createdDate: currentTime,
      updatedDate: currentTime,
    };
  }

  private convertFeatureFlagRawValueToFeatureFlag(
    rawFeatureFlag: FeatureFlagFormRawValue | NewFeatureFlagFormRawValue,
  ): IFeatureFlag | NewFeatureFlag {
    return {
      ...rawFeatureFlag,
      createdDate: dayjs(rawFeatureFlag.createdDate, DATE_TIME_FORMAT),
      updatedDate: dayjs(rawFeatureFlag.updatedDate, DATE_TIME_FORMAT),
    };
  }

  private convertFeatureFlagToFeatureFlagRawValue(
    featureFlag: IFeatureFlag | (Partial<NewFeatureFlag> & FeatureFlagFormDefaults),
  ): FeatureFlagFormRawValue | PartialWithRequiredKeyOf<NewFeatureFlagFormRawValue> {
    return {
      ...featureFlag,
      createdDate: featureFlag.createdDate ? featureFlag.createdDate.format(DATE_TIME_FORMAT) : undefined,
      updatedDate: featureFlag.updatedDate ? featureFlag.updatedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
