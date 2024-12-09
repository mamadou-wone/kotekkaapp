import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: '394db63e-62d3-4421-9808-e7ab28888696',
};

export const sampleWithPartialData: IAuthority = {
  name: '20fecdb4-d08e-417a-b7e5-8c2222fced99',
};

export const sampleWithFullData: IAuthority = {
  name: 'cce598f1-ca4b-4a2b-b1ca-622e7342e07f',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
