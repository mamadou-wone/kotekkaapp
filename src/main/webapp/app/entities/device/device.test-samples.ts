import dayjs from 'dayjs/esm';

import { IDevice, NewDevice } from './device.model';

export const sampleWithRequiredData: IDevice = {
  id: 15242,
  uuid: '6cb56ade-660a-4cad-884b-f2e3785d62be',
};

export const sampleWithPartialData: IDevice = {
  id: 297,
  uuid: 'd1d4f7b5-c47a-4191-81a9-47392f000e3e',
  model: 'alb avow',
  os: 'eek bleach',
  createdBy: 'clinking awful',
  createdDate: dayjs('2024-12-09T10:24'),
};

export const sampleWithFullData: IDevice = {
  id: 4490,
  uuid: '8cd1fdf9-31b8-4d4b-8851-2eec9a0a6805',
  deviceUuid: 'whole aw',
  type: 'beyond aha even',
  manufacturer: 'brr',
  model: 'misreport plus redevelop',
  os: 'beneath',
  appVersion: 'enrage',
  inactive: false,
  createdBy: 'yesterday heavily till',
  createdDate: dayjs('2024-12-08T18:19'),
  lastModifiedBy: 'among schnitzel boohoo',
  lastModifiedDate: dayjs('2024-12-08T20:20'),
};

export const sampleWithNewData: NewDevice = {
  uuid: 'f51f0a02-5ed0-4ecc-b61b-a251449b8bbb',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
