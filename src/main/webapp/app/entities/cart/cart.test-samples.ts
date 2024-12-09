import dayjs from 'dayjs/esm';

import { ICart, NewCart } from './cart.model';

export const sampleWithRequiredData: ICart = {
  id: 26963,
  uuid: '68e8225b-c453-4b3a-9d1a-5c03ac88ae3f',
  walletHolder: 'bc74b385-996f-46c9-9e7f-2542c6c5b82d',
  totalQuantity: 31349,
  totalPrice: 8200.1,
};

export const sampleWithPartialData: ICart = {
  id: 6596,
  uuid: '0cf64ea1-a236-42cd-b362-b0fc114b7418',
  walletHolder: 'e760a671-3cd2-422b-b0ff-6b4165e5370c',
  totalQuantity: 14403,
  totalPrice: 14635,
};

export const sampleWithFullData: ICart = {
  id: 29466,
  uuid: '588047f1-caca-41ba-8c17-95c334d57a0f',
  walletHolder: '45deb08f-52ba-4355-9a87-5049949b22ff',
  totalQuantity: 15960,
  totalPrice: 30710.68,
  currency: 'fax',
  createdDate: dayjs('2024-12-08T15:02'),
  lastModifiedDate: dayjs('2024-12-09T02:49'),
};

export const sampleWithNewData: NewCart = {
  uuid: 'd926e355-de0d-404a-a9d5-91e109538884',
  walletHolder: '39e84f4b-94df-4cc8-8488-9699dad2ee6d',
  totalQuantity: 12803,
  totalPrice: 3388.04,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
