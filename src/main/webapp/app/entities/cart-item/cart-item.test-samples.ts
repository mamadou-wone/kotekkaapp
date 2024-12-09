import { ICartItem, NewCartItem } from './cart-item.model';

export const sampleWithRequiredData: ICartItem = {
  id: 4211,
  uuid: 'b192e8a8-d2f4-4940-b652-316da9f4664f',
  cart: 'ca6d6ad5-c45a-4a31-ae1b-31035203372c',
  product: '7762b308-41fe-4c67-b2a1-67646ce1b6cd',
  quantity: 8428,
  price: 15204.7,
  totalPrice: 26114.44,
};

export const sampleWithPartialData: ICartItem = {
  id: 2489,
  uuid: 'd90ec354-b750-4d15-b0e7-80138e7d7a1e',
  cart: 'd0f3e4db-6c61-4f04-98bd-2331f9ddba05',
  product: '34556786-e5ed-4ac0-ab2e-39b7349e8e57',
  quantity: 22966,
  price: 12782.36,
  totalPrice: 24516.56,
};

export const sampleWithFullData: ICartItem = {
  id: 3469,
  uuid: '7ac2928a-0e3e-4184-890f-e183224c8dce',
  cart: 'ea6a2c8b-27fd-4c7a-8598-8a4192d10ae2',
  product: '79c58d1a-e325-4421-965b-cc859089215e',
  quantity: 27833,
  price: 18945.38,
  totalPrice: 7741.04,
};

export const sampleWithNewData: NewCartItem = {
  uuid: '6b3de254-fd73-4ab4-a2ae-97a1a6cd0ee2',
  cart: '3a1b9e15-b297-432b-99ec-0415a339b300',
  product: '96d4df27-08e5-43ef-97e3-5fd409e8102a',
  quantity: 27918,
  price: 18032.74,
  totalPrice: 5726.55,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
