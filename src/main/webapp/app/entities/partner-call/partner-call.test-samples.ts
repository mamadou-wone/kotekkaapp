import dayjs from 'dayjs/esm';

import { IPartnerCall, NewPartnerCall } from './partner-call.model';

export const sampleWithRequiredData: IPartnerCall = {
  id: 8728,
  partner: 'OTHER',
};

export const sampleWithPartialData: IPartnerCall = {
  id: 20927,
  partner: 'WAFR',
  requestHeaders: '../fake-data/blob/hipster.txt',
  requestBody: '../fake-data/blob/hipster.txt',
  requestTime: dayjs('2024-12-09T04:44'),
  responseHeaders: '../fake-data/blob/hipster.txt',
};

export const sampleWithFullData: IPartnerCall = {
  id: 16538,
  partner: 'WAFR',
  api: 'reconsideration embossing',
  method: 'POST',
  requestHeaders: '../fake-data/blob/hipster.txt',
  requestBody: '../fake-data/blob/hipster.txt',
  requestTime: dayjs('2024-12-08T23:59'),
  responseStatusCode: 466,
  responseHeaders: '../fake-data/blob/hipster.txt',
  responseBody: '../fake-data/blob/hipster.txt',
  responseTime: dayjs('2024-12-09T12:23'),
  correlationId: 'pine conservation',
  queryParam: 'icebreaker now curly',
};

export const sampleWithNewData: NewPartnerCall = {
  partner: 'OTHER',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
