import dayjs from 'dayjs/esm';

import { IOrganisation, NewOrganisation } from './organisation.model';

export const sampleWithRequiredData: IOrganisation = {
  id: 875,
  name: 'oh mature flimsy',
};

export const sampleWithPartialData: IOrganisation = {
  id: 13041,
  name: 'sprinkles whereas',
  parent: 'even because',
  headcount: 23932,
};

export const sampleWithFullData: IOrganisation = {
  id: 24122,
  name: 'ew',
  type: 'UNIVERSITY',
  parent: 'blowgun institute voluntarily',
  location: 'hmph retrospectivity',
  headcount: 3386,
  createdBy: 'whistle elastic',
  createdDate: dayjs('2024-12-09T11:13'),
};

export const sampleWithNewData: NewOrganisation = {
  name: 'overconfidently mmm',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
