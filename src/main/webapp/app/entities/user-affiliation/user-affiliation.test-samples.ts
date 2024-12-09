import dayjs from 'dayjs/esm';

import { IUserAffiliation, NewUserAffiliation } from './user-affiliation.model';

export const sampleWithRequiredData: IUserAffiliation = {
  id: 7225,
  walletHolder: '9ad2613c-f77a-4077-9b10-5918a04c4b48',
  affiliation: 'mid finally',
};

export const sampleWithPartialData: IUserAffiliation = {
  id: 26310,
  walletHolder: '8722f324-3bb1-4994-8e6b-9048cebd16b3',
  affiliation: 'colorless',
  createdBy: 'enlist unless whoa',
};

export const sampleWithFullData: IUserAffiliation = {
  id: 31798,
  walletHolder: '4a8d906c-9272-43df-83ee-1a1bac5ef509',
  affiliation: 'educated softly ruddy',
  createdBy: 'unsightly spotless',
  createdDate: dayjs('2024-12-09T06:41'),
};

export const sampleWithNewData: NewUserAffiliation = {
  walletHolder: '4ef31bdc-8c25-4836-b61b-983420c32a22',
  affiliation: 'gladly wherever beautifully',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
