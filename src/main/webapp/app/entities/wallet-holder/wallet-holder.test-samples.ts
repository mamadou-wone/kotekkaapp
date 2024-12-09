import dayjs from 'dayjs/esm';

import { IWalletHolder, NewWalletHolder } from './wallet-holder.model';

export const sampleWithRequiredData: IWalletHolder = {
  id: 16560,
  uuid: '04e3d8eb-fadb-46fb-bbde-3ae12da0977d',
};

export const sampleWithPartialData: IWalletHolder = {
  id: 865,
  uuid: 'f761e68d-f36a-4b73-a08c-46ddf480ba40',
  status: 'REJECTED',
  phoneNumber: 'sand um huzzah',
  network: 'OTHER',
  tag: 'publicity cheerful',
  firstName: 'Jaida',
  lastName: 'Bayer',
  postalCode: 'in freely',
  onboarding: 'CONFIRM_OTP',
  dateOfBirth: dayjs('2024-12-08'),
  createdBy: 'arid over etch',
  loginStatus: 'LOGIN_LOCKED',
};

export const sampleWithFullData: IWalletHolder = {
  id: 1541,
  uuid: 'e054cec3-ccbc-40fd-8be8-74c7925ff68f',
  type: 'CUSTOMER',
  status: 'CLOSED',
  phoneNumber: 'while feather',
  network: 'ORANGE',
  tag: 'hope pfft',
  firstName: 'Mara',
  lastName: 'Adams',
  address: 'helpful not',
  city: 'Collierville',
  country: 'Barbados',
  postalCode: 'fathom adult clamp',
  onboarding: 'SET_PASSWORD',
  externalId: 'dapper before',
  email: 'Trevion_Watsica@gmail.com',
  dateOfBirth: dayjs('2024-12-09'),
  sex: 'F',
  createdBy: 'finally ill uh-huh',
  createdDate: dayjs('2024-12-08T22:01'),
  lastModifiedBy: 'up unique finally',
  lastModifiedDate: dayjs('2024-12-08T21:33'),
  loginStatus: 'LOGIN_DEVICE_LOCKED',
};

export const sampleWithNewData: NewWalletHolder = {
  uuid: '8a97543a-76f0-4a0d-88be-b9bece20bec7',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
