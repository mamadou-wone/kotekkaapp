import dayjs from 'dayjs/esm';

import { IMoneyRequest, NewMoneyRequest } from './money-request.model';

export const sampleWithRequiredData: IMoneyRequest = {
  id: 9364,
  uuid: 'c285c0d1-4601-48ac-8765-f7f8a120ba12',
};

export const sampleWithPartialData: IMoneyRequest = {
  id: 14776,
  uuid: 'f0fa83c1-1d2d-4953-8c81-ed13ebd6dd67',
  otherHolder: '01270785-b50a-449d-8772-e10917f1990f',
  description: 'versus',
  createdDate: dayjs('2024-12-09T10:29'),
  lastModifiedDate: dayjs('2024-12-08T23:14'),
};

export const sampleWithFullData: IMoneyRequest = {
  id: 5152,
  uuid: '905ec507-7422-49ca-ad96-9374c752c30d',
  status: 'FAILED',
  otherHolder: '168395db-71fc-4821-a05a-ef8de5ea09b2',
  amount: 20271.95,
  description: 'gadzooks sans vastly',
  currency: 'ast',
  walletHolder: '2f58685b-e8d3-47c9-9b7c-c432dc86ebdd',
  createdBy: 'fold wherever pulverize',
  createdDate: dayjs('2024-12-08T23:44'),
  lastModifiedBy: 'pro',
  lastModifiedDate: dayjs('2024-12-08T18:53'),
};

export const sampleWithNewData: NewMoneyRequest = {
  uuid: '999322dc-fde3-4b4d-8bf7-df744b12485d',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
