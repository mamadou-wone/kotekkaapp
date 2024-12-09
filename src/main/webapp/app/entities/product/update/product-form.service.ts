import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IProduct, NewProduct } from '../product.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProduct for edit and NewProductFormGroupInput for create.
 */
type ProductFormGroupInput = IProduct | PartialWithRequiredKeyOf<NewProduct>;

type ProductFormDefaults = Pick<NewProduct, 'id' | 'trackQuantity' | 'collections'>;

type ProductFormGroupContent = {
  id: FormControl<IProduct['id'] | NewProduct['id']>;
  uuid: FormControl<IProduct['uuid']>;
  walletHolder: FormControl<IProduct['walletHolder']>;
  title: FormControl<IProduct['title']>;
  description: FormControl<IProduct['description']>;
  status: FormControl<IProduct['status']>;
  media: FormControl<IProduct['media']>;
  mediaContentType: FormControl<IProduct['mediaContentType']>;
  price: FormControl<IProduct['price']>;
  compareAtPrice: FormControl<IProduct['compareAtPrice']>;
  costPerItem: FormControl<IProduct['costPerItem']>;
  profit: FormControl<IProduct['profit']>;
  margin: FormControl<IProduct['margin']>;
  inventoryQuantity: FormControl<IProduct['inventoryQuantity']>;
  inventoryLocation: FormControl<IProduct['inventoryLocation']>;
  trackQuantity: FormControl<IProduct['trackQuantity']>;
  category: FormControl<IProduct['category']>;
  collections: FormControl<IProduct['collections']>;
};

export type ProductFormGroup = FormGroup<ProductFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProductFormService {
  createProductFormGroup(product: ProductFormGroupInput = { id: null }): ProductFormGroup {
    const productRawValue = {
      ...this.getFormDefaults(),
      ...product,
    };
    return new FormGroup<ProductFormGroupContent>({
      id: new FormControl(
        { value: productRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      uuid: new FormControl(productRawValue.uuid, {
        validators: [Validators.required],
      }),
      walletHolder: new FormControl(productRawValue.walletHolder),
      title: new FormControl(productRawValue.title, {
        validators: [Validators.required],
      }),
      description: new FormControl(productRawValue.description, {
        validators: [Validators.required, Validators.maxLength(5000)],
      }),
      status: new FormControl(productRawValue.status, {
        validators: [Validators.required],
      }),
      media: new FormControl(productRawValue.media),
      mediaContentType: new FormControl(productRawValue.mediaContentType),
      price: new FormControl(productRawValue.price, {
        validators: [Validators.required],
      }),
      compareAtPrice: new FormControl(productRawValue.compareAtPrice),
      costPerItem: new FormControl(productRawValue.costPerItem),
      profit: new FormControl(productRawValue.profit),
      margin: new FormControl(productRawValue.margin),
      inventoryQuantity: new FormControl(productRawValue.inventoryQuantity),
      inventoryLocation: new FormControl(productRawValue.inventoryLocation),
      trackQuantity: new FormControl(productRawValue.trackQuantity),
      category: new FormControl(productRawValue.category),
      collections: new FormControl(productRawValue.collections ?? []),
    });
  }

  getProduct(form: ProductFormGroup): IProduct | NewProduct {
    return form.getRawValue() as IProduct | NewProduct;
  }

  resetForm(form: ProductFormGroup, product: ProductFormGroupInput): void {
    const productRawValue = { ...this.getFormDefaults(), ...product };
    form.reset(
      {
        ...productRawValue,
        id: { value: productRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ProductFormDefaults {
    return {
      id: null,
      trackQuantity: false,
      collections: [],
    };
  }
}
