import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: 1001,
  login: 'FkpAg',
};

export const sampleWithPartialData: IUser = {
  id: 19213,
  login: 'AO@9P7\\ki\\b0LIC\\|l\\fs',
};

export const sampleWithFullData: IUser = {
  id: 25938,
  login: '-',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
