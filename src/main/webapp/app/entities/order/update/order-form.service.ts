import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IOrder, NewOrder } from '../order.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOrder for edit and NewOrderFormGroupInput for create.
 */
type OrderFormGroupInput = IOrder | PartialWithRequiredKeyOf<NewOrder>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IOrder | NewOrder> = Omit<T, 'orderDate' | 'createdDate' | 'lastModifiedDate'> & {
  orderDate?: string | null;
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type OrderFormRawValue = FormValueOf<IOrder>;

type NewOrderFormRawValue = FormValueOf<NewOrder>;

type OrderFormDefaults = Pick<NewOrder, 'id' | 'orderDate' | 'createdDate' | 'lastModifiedDate'>;

type OrderFormGroupContent = {
  id: FormControl<OrderFormRawValue['id'] | NewOrder['id']>;
  uuid: FormControl<OrderFormRawValue['uuid']>;
  walletHolder: FormControl<OrderFormRawValue['walletHolder']>;
  status: FormControl<OrderFormRawValue['status']>;
  totalPrice: FormControl<OrderFormRawValue['totalPrice']>;
  currency: FormControl<OrderFormRawValue['currency']>;
  orderDate: FormControl<OrderFormRawValue['orderDate']>;
  paymentMethod: FormControl<OrderFormRawValue['paymentMethod']>;
  shippingAddress: FormControl<OrderFormRawValue['shippingAddress']>;
  createdBy: FormControl<OrderFormRawValue['createdBy']>;
  createdDate: FormControl<OrderFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<OrderFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<OrderFormRawValue['lastModifiedDate']>;
};

export type OrderFormGroup = FormGroup<OrderFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class OrderFormService {
  createOrderFormGroup(order: OrderFormGroupInput = { id: null }): OrderFormGroup {
    const orderRawValue = this.convertOrderToOrderRawValue({
      ...this.getFormDefaults(),
      ...order,
    });
    return new FormGroup<OrderFormGroupContent>({
      id: new FormControl(
        { value: orderRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      uuid: new FormControl(orderRawValue.uuid, {
        validators: [Validators.required],
      }),
      walletHolder: new FormControl(orderRawValue.walletHolder),
      status: new FormControl(orderRawValue.status, {
        validators: [Validators.required],
      }),
      totalPrice: new FormControl(orderRawValue.totalPrice, {
        validators: [Validators.required],
      }),
      currency: new FormControl(orderRawValue.currency, {
        validators: [Validators.maxLength(3)],
      }),
      orderDate: new FormControl(orderRawValue.orderDate, {
        validators: [Validators.required],
      }),
      paymentMethod: new FormControl(orderRawValue.paymentMethod, {
        validators: [Validators.maxLength(50)],
      }),
      shippingAddress: new FormControl(orderRawValue.shippingAddress),
      createdBy: new FormControl(orderRawValue.createdBy),
      createdDate: new FormControl(orderRawValue.createdDate),
      lastModifiedBy: new FormControl(orderRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(orderRawValue.lastModifiedDate),
    });
  }

  getOrder(form: OrderFormGroup): IOrder | NewOrder {
    return this.convertOrderRawValueToOrder(form.getRawValue() as OrderFormRawValue | NewOrderFormRawValue);
  }

  resetForm(form: OrderFormGroup, order: OrderFormGroupInput): void {
    const orderRawValue = this.convertOrderToOrderRawValue({ ...this.getFormDefaults(), ...order });
    form.reset(
      {
        ...orderRawValue,
        id: { value: orderRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): OrderFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      orderDate: currentTime,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertOrderRawValueToOrder(rawOrder: OrderFormRawValue | NewOrderFormRawValue): IOrder | NewOrder {
    return {
      ...rawOrder,
      orderDate: dayjs(rawOrder.orderDate, DATE_TIME_FORMAT),
      createdDate: dayjs(rawOrder.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawOrder.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertOrderToOrderRawValue(
    order: IOrder | (Partial<NewOrder> & OrderFormDefaults),
  ): OrderFormRawValue | PartialWithRequiredKeyOf<NewOrderFormRawValue> {
    return {
      ...order,
      orderDate: order.orderDate ? order.orderDate.format(DATE_TIME_FORMAT) : undefined,
      createdDate: order.createdDate ? order.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: order.lastModifiedDate ? order.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
