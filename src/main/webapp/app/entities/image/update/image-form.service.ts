import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IImage, NewImage } from '../image.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IImage for edit and NewImageFormGroupInput for create.
 */
type ImageFormGroupInput = IImage | PartialWithRequiredKeyOf<NewImage>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IImage | NewImage> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type ImageFormRawValue = FormValueOf<IImage>;

type NewImageFormRawValue = FormValueOf<NewImage>;

type ImageFormDefaults = Pick<NewImage, 'id' | 'createdDate' | 'lastModifiedDate'>;

type ImageFormGroupContent = {
  id: FormControl<ImageFormRawValue['id'] | NewImage['id']>;
  uuid: FormControl<ImageFormRawValue['uuid']>;
  name: FormControl<ImageFormRawValue['name']>;
  file: FormControl<ImageFormRawValue['file']>;
  fileContentType: FormControl<ImageFormRawValue['fileContentType']>;
  walletHolder: FormControl<ImageFormRawValue['walletHolder']>;
  createdBy: FormControl<ImageFormRawValue['createdBy']>;
  createdDate: FormControl<ImageFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<ImageFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<ImageFormRawValue['lastModifiedDate']>;
};

export type ImageFormGroup = FormGroup<ImageFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ImageFormService {
  createImageFormGroup(image: ImageFormGroupInput = { id: null }): ImageFormGroup {
    const imageRawValue = this.convertImageToImageRawValue({
      ...this.getFormDefaults(),
      ...image,
    });
    return new FormGroup<ImageFormGroupContent>({
      id: new FormControl(
        { value: imageRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      uuid: new FormControl(imageRawValue.uuid, {
        validators: [Validators.required],
      }),
      name: new FormControl(imageRawValue.name, {
        validators: [Validators.maxLength(100)],
      }),
      file: new FormControl(imageRawValue.file),
      fileContentType: new FormControl(imageRawValue.fileContentType),
      walletHolder: new FormControl(imageRawValue.walletHolder),
      createdBy: new FormControl(imageRawValue.createdBy),
      createdDate: new FormControl(imageRawValue.createdDate),
      lastModifiedBy: new FormControl(imageRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(imageRawValue.lastModifiedDate),
    });
  }

  getImage(form: ImageFormGroup): IImage | NewImage {
    return this.convertImageRawValueToImage(form.getRawValue() as ImageFormRawValue | NewImageFormRawValue);
  }

  resetForm(form: ImageFormGroup, image: ImageFormGroupInput): void {
    const imageRawValue = this.convertImageToImageRawValue({ ...this.getFormDefaults(), ...image });
    form.reset(
      {
        ...imageRawValue,
        id: { value: imageRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ImageFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertImageRawValueToImage(rawImage: ImageFormRawValue | NewImageFormRawValue): IImage | NewImage {
    return {
      ...rawImage,
      createdDate: dayjs(rawImage.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawImage.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertImageToImageRawValue(
    image: IImage | (Partial<NewImage> & ImageFormDefaults),
  ): ImageFormRawValue | PartialWithRequiredKeyOf<NewImageFormRawValue> {
    return {
      ...image,
      createdDate: image.createdDate ? image.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: image.lastModifiedDate ? image.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
