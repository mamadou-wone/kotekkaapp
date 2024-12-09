import dayjs from 'dayjs/esm';

import { ITransaction, NewTransaction } from './transaction.model';

export const sampleWithRequiredData: ITransaction = {
  id: 1012,
  uuid: '755d5314-9861-44cb-af93-f8d12d7e99d2',
};

export const sampleWithPartialData: ITransaction = {
  id: 18716,
  uuid: '674c4e45-bf61-4ba1-97ed-16842fa3cbe4',
  direction: 'DEBIT',
  amount: 21530.69,
  description: 'longingly loftily',
  fee: 16305.07,
  commission: 13026.28,
  currency: 'afo',
  counterpartyType: 'WALLET',
  effectiveDate: dayjs('2024-12-08'),
  startTime: dayjs('2024-12-08T23:13'),
  partnerToken: 'ugh',
  wallet: 'de14876c-6654-4e8b-85e4-b1db64bc897e',
  createdDate: dayjs('2024-12-08T14:29'),
  lastModifiedBy: 'dreamily',
  lastModifiedDate: dayjs('2024-12-08T15:02'),
};

export const sampleWithFullData: ITransaction = {
  id: 4479,
  uuid: 'b957afaf-a78e-4e82-a683-0f8377cc71de',
  type: 'FEE',
  status: 'PARTNER_REJECTED',
  direction: 'CREDIT',
  amount: 10422.22,
  description: 'for vast',
  fee: 19728.76,
  commission: 1681.36,
  currency: 'gia',
  counterpartyType: 'WALLET',
  counterpartyId: 'partial even ultimately',
  entryDate: dayjs('2024-12-08'),
  effectiveDate: dayjs('2024-12-09'),
  startTime: dayjs('2024-12-08T22:19'),
  endTime: dayjs('2024-12-08T13:38'),
  parent: '4d40603d-3af8-4d17-b7f5-a9a7a8536372',
  reference: 'ferret hmph',
  externalId: 'lest',
  partnerToken: 'alarmed',
  wallet: '01b289c4-4d5f-4078-aa41-575d0f363780',
  createdBy: 'pish simple',
  createdDate: dayjs('2024-12-09T02:29'),
  lastModifiedBy: 'hateful criminal nor',
  lastModifiedDate: dayjs('2024-12-09T05:39'),
};

export const sampleWithNewData: NewTransaction = {
  uuid: '49cbda80-8634-4772-a6ea-4eaf5250dc66',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
