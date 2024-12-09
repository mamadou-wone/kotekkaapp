import { ICategory, NewCategory } from './category.model';

export const sampleWithRequiredData: ICategory = {
  id: 14647,
  name: 'during hierarchy nougat',
};

export const sampleWithPartialData: ICategory = {
  id: 13410,
  name: 'exactly vista ah',
};

export const sampleWithFullData: ICategory = {
  id: 8961,
  name: 'recovery',
};

export const sampleWithNewData: NewCategory = {
  name: 'unbearably doorpost',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
