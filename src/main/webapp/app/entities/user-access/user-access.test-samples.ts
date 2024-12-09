import dayjs from 'dayjs/esm';

import { IUserAccess, NewUserAccess } from './user-access.model';

export const sampleWithRequiredData: IUserAccess = {
  id: 23153,
};

export const sampleWithPartialData: IUserAccess = {
  id: 11765,
  login: 'fooey incidentally fondly',
  ipAddress: 'duh tremendously',
  device: 'b9034581-182b-4874-94af-f1066f46f2e1',
};

export const sampleWithFullData: IUserAccess = {
  id: 4519,
  login: 'hmph between than',
  ipAddress: 'serialize clear',
  device: '017c1b4d-5831-4e28-9fb5-587a219357ef',
  createdDate: dayjs('2024-12-09T01:17'),
};

export const sampleWithNewData: NewUserAccess = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
