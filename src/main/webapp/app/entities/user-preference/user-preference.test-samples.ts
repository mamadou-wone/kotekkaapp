import dayjs from 'dayjs/esm';

import { IUserPreference, NewUserPreference } from './user-preference.model';

export const sampleWithRequiredData: IUserPreference = {
  id: 27990,
};

export const sampleWithPartialData: IUserPreference = {
  id: 1636,
  createdDate: dayjs('2024-12-09T02:03'),
};

export const sampleWithFullData: IUserPreference = {
  id: 4014,
  app: 'PORTAL',
  name: 'quarrelsomely selfishly',
  setting: 'typeface unfit yogurt',
  createdDate: dayjs('2024-12-09T02:06'),
};

export const sampleWithNewData: NewUserPreference = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
