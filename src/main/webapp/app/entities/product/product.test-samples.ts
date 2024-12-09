import { IProduct, NewProduct } from './product.model';

export const sampleWithRequiredData: IProduct = {
  id: 16887,
  uuid: 'c8ed8511-e7f0-44c0-b0b7-53b412a9f50e',
  title: 'between airport settler',
  description: 'gadzooks tentacle underneath',
  status: 'Inactive',
  price: 9501.6,
};

export const sampleWithPartialData: IProduct = {
  id: 8843,
  uuid: 'aea1ba70-75a6-4952-ab99-fb8069e51cd5',
  title: 'bah',
  description: 'controvert inquisitively',
  status: 'Inactive',
  media: '../fake-data/blob/hipster.png',
  mediaContentType: 'unknown',
  price: 26615.24,
  compareAtPrice: 7928.41,
  costPerItem: 10850.4,
  inventoryQuantity: 25370,
};

export const sampleWithFullData: IProduct = {
  id: 407,
  uuid: '360e7049-4883-4d06-b5f6-79dd658baa07',
  walletHolder: '89334d25-e5cb-4877-a52c-20d74ad3c7da',
  title: 'blah impact',
  description: 'procrastinate',
  status: 'Active',
  media: '../fake-data/blob/hipster.png',
  mediaContentType: 'unknown',
  price: 6957.42,
  compareAtPrice: 17747.77,
  costPerItem: 29146.73,
  profit: 23575.96,
  margin: 19084.47,
  inventoryQuantity: 6786,
  inventoryLocation: 'designation kiddingly',
  trackQuantity: true,
};

export const sampleWithNewData: NewProduct = {
  uuid: 'd6a7af4c-fdef-4f4e-8efa-4eb6730081ad',
  title: 'refer',
  description: 'disadvantage even kissingly',
  status: 'Inactive',
  price: 1370.7,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
