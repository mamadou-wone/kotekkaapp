import dayjs from 'dayjs/esm';

import { IWallet, NewWallet } from './wallet.model';

export const sampleWithRequiredData: IWallet = {
  id: 9020,
  uuid: 'a88c8fc4-8a8c-468a-9f35-1dc6c8bb2287',
};

export const sampleWithPartialData: IWallet = {
  id: 4256,
  uuid: '0db8d0ce-9d9a-4392-a24f-8d6aad6bbaa2',
  type: 'SAVINGS',
  iban: 'PK58BDCD3300701050879105',
  balance: 428.96,
  balancesAsOf: dayjs('2024-12-08T21:30'),
  createdBy: 'clear',
  createdDate: dayjs('2024-12-09T07:14'),
};

export const sampleWithFullData: IWallet = {
  id: 17966,
  uuid: '93f10ac8-7577-4ca1-997b-9ad9a4d5ab69',
  type: 'SAVINGS',
  status: 'ACTIVE',
  level: 'ONE',
  iban: 'JO28LNAG6537248355299749399093',
  currency: 'ins',
  balance: 25983.75,
  balancesAsOf: dayjs('2024-12-09T04:39'),
  externalId: 'well-lit yieldingly forearm',
  walletHolder: '4e1348b2-0299-4fe8-8ccc-b9bbd5c1246f',
  createdBy: 'expostulate',
  createdDate: dayjs('2024-12-08T22:43'),
  lastModifiedBy: 'willfully mushy upliftingly',
  lastModifiedDate: dayjs('2024-12-09T02:53'),
};

export const sampleWithNewData: NewWallet = {
  uuid: 'e0e64d79-1ce5-4eee-a9e4-dfcb589756e5',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
