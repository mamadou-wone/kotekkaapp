import dayjs from 'dayjs/esm';

import { IServiceClient, NewServiceClient } from './service-client.model';

export const sampleWithRequiredData: IServiceClient = {
  id: 19376,
};

export const sampleWithPartialData: IServiceClient = {
  id: 14855,
  apiKey: 'a7e7fcca-8817-4e56-97cd-9389df6466e2',
};

export const sampleWithFullData: IServiceClient = {
  id: 18318,
  clientId: '9287417f-491b-49b4-af93-0c647dac5b3b',
  type: 'INTERNAL',
  apiKey: 'c9c10dfc-06f3-47da-9a73-f9e0cd56510b',
  status: 'BLOCKED',
  note: 'meh aw',
  createdDate: dayjs('2024-12-09T06:51'),
  lastModifiedDate: dayjs('2024-12-08T20:09'),
};

export const sampleWithNewData: NewServiceClient = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
