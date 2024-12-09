import dayjs from 'dayjs/esm';

import { IDiscount, NewDiscount } from './discount.model';

export const sampleWithRequiredData: IDiscount = {
  id: 32492,
  uuid: '1ddfcc2f-cbb3-48d6-8058-f86ddebb9465',
  name: 'out',
  type: 'FIXED',
  value: 19309.9,
};

export const sampleWithPartialData: IDiscount = {
  id: 13078,
  uuid: 'de6106fb-0096-4299-bf37-6bd3740ad934',
  name: 'zebra wearily by',
  type: 'FIXED',
  value: 24816.55,
  endDate: dayjs('2024-12-09T11:57'),
  status: 'DELETED',
  createdDate: dayjs('2024-12-09T13:02'),
};

export const sampleWithFullData: IDiscount = {
  id: 8698,
  uuid: '52f61b37-e57e-47ed-a03d-0e694f6e5544',
  name: 'shyly yet uncork',
  type: 'FIXED',
  value: 3846.94,
  startDate: dayjs('2024-12-08T13:29'),
  endDate: dayjs('2024-12-09T00:53'),
  status: 'ACTIVE',
  createdBy: 'skyscraper lovable',
  createdDate: dayjs('2024-12-08T16:26'),
  lastModifiedBy: 'miserable',
  lastModifiedDate: dayjs('2024-12-08T16:51'),
};

export const sampleWithNewData: NewDiscount = {
  uuid: '6e3c818a-1810-4b76-9636-2ad5551ac77f',
  name: 'whoa',
  type: 'FIXED',
  value: 15924.52,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
