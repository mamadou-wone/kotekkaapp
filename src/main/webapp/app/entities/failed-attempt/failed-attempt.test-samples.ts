import dayjs from 'dayjs/esm';

import { IFailedAttempt, NewFailedAttempt } from './failed-attempt.model';

export const sampleWithRequiredData: IFailedAttempt = {
  id: 23902,
};

export const sampleWithPartialData: IFailedAttempt = {
  id: 29243,
  isAfterLock: false,
  action: 'PASSWORD_RESET',
  device: '637734a7-7752-4244-92c5-782b2687da69',
  reason: 'overproduce',
};

export const sampleWithFullData: IFailedAttempt = {
  id: 20157,
  login: 'brr enfold self-reliant',
  ipAddress: 'keenly emergent',
  isAfterLock: false,
  app: 'PORTAL',
  action: 'EMAIL_CHECK',
  device: '0cd9fd66-8868-4c58-ab9e-071d98296395',
  createdDate: dayjs('2024-12-09T07:24'),
  reason: 'how physically ashamed',
};

export const sampleWithNewData: NewFailedAttempt = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
