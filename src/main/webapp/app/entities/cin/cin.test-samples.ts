import dayjs from 'dayjs/esm';

import { ICin, NewCin } from './cin.model';

export const sampleWithRequiredData: ICin = {
  id: 10469,
};

export const sampleWithPartialData: ICin = {
  id: 2402,
  cinId: 'wherever',
  validityDate: dayjs('2024-12-08'),
  firstName: 'Marco',
  lastName: 'Blick',
  fatherName: 'meanwhile',
  nationality: 'joyous following noted',
  issuingCountry: 'including atop apropos',
  civilRegister: 'austere',
  sex: 'bro',
  birthCityCode: 'foo',
  walletHolder: 'fb2f08d5-64a8-4143-b2b4-0a3f9af41e8d',
};

export const sampleWithFullData: ICin = {
  id: 26570,
  cinId: 'forenenst',
  validityDate: dayjs('2024-12-09'),
  birthDate: dayjs('2024-12-08'),
  birthPlace: 'barring archaeology',
  firstName: 'Donnie',
  lastName: 'Wisozk',
  birthCity: 'mmm boohoo',
  fatherName: 'since afore eek',
  nationality: 'ha',
  nationalityCode: 'ha than',
  issuingCountry: 'extremely',
  issuingCountryCode: 'les',
  motherName: 'wherever',
  civilRegister: 'phew below',
  sex: 'apr',
  address: 'sway brr hm',
  birthCityCode: 'mas',
  walletHolder: 'd32658b6-1ae5-447e-b4e8-68b7ca0b71d3',
  createdDate: dayjs('2024-12-09T11:10'),
};

export const sampleWithNewData: NewCin = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
