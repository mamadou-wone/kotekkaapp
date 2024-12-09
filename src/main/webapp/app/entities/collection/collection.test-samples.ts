import { ICollection, NewCollection } from './collection.model';

export const sampleWithRequiredData: ICollection = {
  id: 14444,
  name: 'artistic loftily intrigue',
};

export const sampleWithPartialData: ICollection = {
  id: 22931,
  name: 'gah',
};

export const sampleWithFullData: ICollection = {
  id: 1989,
  name: 'before',
};

export const sampleWithNewData: NewCollection = {
  name: 'ouch masquerade unless',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
