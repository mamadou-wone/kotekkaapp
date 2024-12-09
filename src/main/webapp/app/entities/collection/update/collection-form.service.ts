import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ICollection, NewCollection } from '../collection.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICollection for edit and NewCollectionFormGroupInput for create.
 */
type CollectionFormGroupInput = ICollection | PartialWithRequiredKeyOf<NewCollection>;

type CollectionFormDefaults = Pick<NewCollection, 'id' | 'products'>;

type CollectionFormGroupContent = {
  id: FormControl<ICollection['id'] | NewCollection['id']>;
  name: FormControl<ICollection['name']>;
  products: FormControl<ICollection['products']>;
};

export type CollectionFormGroup = FormGroup<CollectionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CollectionFormService {
  createCollectionFormGroup(collection: CollectionFormGroupInput = { id: null }): CollectionFormGroup {
    const collectionRawValue = {
      ...this.getFormDefaults(),
      ...collection,
    };
    return new FormGroup<CollectionFormGroupContent>({
      id: new FormControl(
        { value: collectionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(collectionRawValue.name, {
        validators: [Validators.required],
      }),
      products: new FormControl(collectionRawValue.products ?? []),
    });
  }

  getCollection(form: CollectionFormGroup): ICollection | NewCollection {
    return form.getRawValue() as ICollection | NewCollection;
  }

  resetForm(form: CollectionFormGroup, collection: CollectionFormGroupInput): void {
    const collectionRawValue = { ...this.getFormDefaults(), ...collection };
    form.reset(
      {
        ...collectionRawValue,
        id: { value: collectionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CollectionFormDefaults {
    return {
      id: null,
      products: [],
    };
  }
}
