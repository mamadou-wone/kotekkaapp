import dayjs from 'dayjs/esm';

import { ICacheInfo, NewCacheInfo } from './cache-info.model';

export const sampleWithRequiredData: ICacheInfo = {
  id: 17895,
};

export const sampleWithPartialData: ICacheInfo = {
  id: 6337,
  key: 'sensitize breakable',
};

export const sampleWithFullData: ICacheInfo = {
  id: 2556,
  walletHolder: 'a7895a31-960f-4c5b-bac4-90e0ea7afdb9',
  key: 'apropos briskly psst',
  value: '../fake-data/blob/hipster.txt',
  createdDate: dayjs('2024-12-09T07:27'),
};

export const sampleWithNewData: NewCacheInfo = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
