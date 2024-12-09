import dayjs from 'dayjs/esm';

import { IFailedAttemptHistory, NewFailedAttemptHistory } from './failed-attempt-history.model';

export const sampleWithRequiredData: IFailedAttemptHistory = {
  id: 10710,
};

export const sampleWithPartialData: IFailedAttemptHistory = {
  id: 18835,
  login: 'rarely sin sugary',
  app: 'MERCHANT',
  action: 'LOGIN',
  createdDate: dayjs('2024-12-08T21:35'),
};

export const sampleWithFullData: IFailedAttemptHistory = {
  id: 9931,
  login: 'hexagon that',
  ipAddress: 'customise',
  isAfterLock: true,
  app: 'PORTAL',
  action: 'PASSWORD_RESET',
  device: '63ea2ae4-2c96-47ef-bca5-bc1aa61b3e4e',
  createdDate: dayjs('2024-12-09T01:21'),
  reason: 'wildly',
};

export const sampleWithNewData: NewFailedAttemptHistory = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
