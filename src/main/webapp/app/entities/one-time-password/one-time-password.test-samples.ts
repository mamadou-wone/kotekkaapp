import dayjs from 'dayjs/esm';

import { IOneTimePassword, NewOneTimePassword } from './one-time-password.model';

export const sampleWithRequiredData: IOneTimePassword = {
  id: 19886,
  uuid: '65363f21-b2d4-435b-92c2-a5f3891d7e9e',
};

export const sampleWithPartialData: IOneTimePassword = {
  id: 14136,
  uuid: '62630b3d-8b5c-468f-a8e7-a9e35c7b2014',
  user: 'stratify',
  expiry: dayjs('2024-12-09T12:58'),
};

export const sampleWithFullData: IOneTimePassword = {
  id: 10478,
  uuid: 'bd072160-f9bd-4699-bd05-706c206cfe9a',
  user: 'smoothly briskly formal',
  code: 'ha',
  status: 'PENDING',
  expiry: dayjs('2024-12-09T10:37'),
  createdDate: dayjs('2024-12-08T15:31'),
};

export const sampleWithNewData: NewOneTimePassword = {
  uuid: '27c575b6-3818-4aa0-8dce-83fde30481f7',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
