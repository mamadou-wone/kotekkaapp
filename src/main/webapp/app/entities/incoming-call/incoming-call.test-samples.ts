import dayjs from 'dayjs/esm';

import { IIncomingCall, NewIncomingCall } from './incoming-call.model';

export const sampleWithRequiredData: IIncomingCall = {
  id: 26855,
};

export const sampleWithPartialData: IIncomingCall = {
  id: 7043,
  partner: 'CMI',
  requestHeaders: '../fake-data/blob/hipster.txt',
  responseStatusCode: 805,
  responseTime: dayjs('2024-12-09T04:22'),
};

export const sampleWithFullData: IIncomingCall = {
  id: 17481,
  partner: 'CIH',
  api: 'home asset or',
  method: 'PUT',
  requestHeaders: '../fake-data/blob/hipster.txt',
  requestBody: '../fake-data/blob/hipster.txt',
  createdDate: dayjs('2024-12-09T09:11'),
  responseStatusCode: 477,
  responseTime: dayjs('2024-12-09T03:41'),
};

export const sampleWithNewData: NewIncomingCall = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
