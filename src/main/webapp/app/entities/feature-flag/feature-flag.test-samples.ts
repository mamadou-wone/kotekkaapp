import dayjs from 'dayjs/esm';

import { IFeatureFlag, NewFeatureFlag } from './feature-flag.model';

export const sampleWithRequiredData: IFeatureFlag = {
  id: 23265,
};

export const sampleWithPartialData: IFeatureFlag = {
  id: 32210,
  name: 'obtrude',
  createdBy: 'woot instead drum',
};

export const sampleWithFullData: IFeatureFlag = {
  id: 26584,
  name: 'thoughtfully',
  enabled: true,
  description: 'hunger inasmuch per',
  createdBy: 'bitter recent',
  createdDate: dayjs('2024-12-08T14:13'),
  updatedBy: 'sundae vary',
  updatedDate: dayjs('2024-12-09T11:55'),
};

export const sampleWithNewData: NewFeatureFlag = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
