import dayjs from 'dayjs/esm';

import { IOrder, NewOrder } from './order.model';

export const sampleWithRequiredData: IOrder = {
  id: 12208,
  uuid: 'a4a45f5a-dea2-40f9-ac30-31d297a858cc',
  status: 'CONFIRMED',
  totalPrice: 286.87,
  orderDate: dayjs('2024-12-08T22:28'),
};

export const sampleWithPartialData: IOrder = {
  id: 9238,
  uuid: '26328af4-7900-4279-8007-af100c2fa0e9',
  walletHolder: '6f265951-c415-4c90-b658-28a8839c0e56',
  status: 'DELIVERED',
  totalPrice: 21506.37,
  orderDate: dayjs('2024-12-09T06:08'),
  paymentMethod: 'quaff how',
  shippingAddress: '../fake-data/blob/hipster.txt',
  createdBy: 'nor stir-fry',
  lastModifiedBy: 'aboard',
};

export const sampleWithFullData: IOrder = {
  id: 27953,
  uuid: 'bff1ce26-c08b-4c2b-adf4-fe8dc3e1b0ae',
  walletHolder: '419142dc-534a-4840-a2e3-648f4b5d00b5',
  status: 'CANCELED',
  totalPrice: 30796.61,
  currency: 'tat',
  orderDate: dayjs('2024-12-09T05:29'),
  paymentMethod: 'upbeat',
  shippingAddress: '../fake-data/blob/hipster.txt',
  createdBy: 'ascertain',
  createdDate: dayjs('2024-12-08T14:48'),
  lastModifiedBy: 'serialize under',
  lastModifiedDate: dayjs('2024-12-08T20:57'),
};

export const sampleWithNewData: NewOrder = {
  uuid: 'dd9793d2-3f9d-483d-bd67-123cb070633d',
  status: 'DELIVERED',
  totalPrice: 20680.1,
  orderDate: dayjs('2024-12-09T08:43'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
