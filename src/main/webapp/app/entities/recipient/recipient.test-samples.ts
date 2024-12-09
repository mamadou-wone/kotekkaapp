import dayjs from 'dayjs/esm';

import { IRecipient, NewRecipient } from './recipient.model';

export const sampleWithRequiredData: IRecipient = {
  id: 31169,
  uuid: '3c4d2537-6c02-4807-84a1-ca08c00ef60d',
};

export const sampleWithPartialData: IRecipient = {
  id: 47,
  uuid: '3b2eda80-9763-4155-aed1-2ec70574b769',
  status: 'DELETED',
  firstName: 'Reanna',
  lastName: 'Skiles',
  phoneNumber: 'debut',
  createdBy: 'spear outfox frenetically',
};

export const sampleWithFullData: IRecipient = {
  id: 13393,
  uuid: '91f84ab2-32e9-4fec-ab11-c9a497165865',
  status: 'BLOCKED',
  firstName: 'Matilda',
  lastName: 'Berge',
  phoneNumber: 'under keel',
  walletHolder: 'd53c09d7-3077-437d-a130-cd65de031018',
  createdBy: 'certainly which though',
  createdDate: dayjs('2024-12-09T12:40'),
  lastModifiedBy: 'famously guilt',
  lastModifiedDate: dayjs('2024-12-08T21:34'),
};

export const sampleWithNewData: NewRecipient = {
  uuid: '8b98d7b9-bbbb-47d0-b06f-b6324c88abe4',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
