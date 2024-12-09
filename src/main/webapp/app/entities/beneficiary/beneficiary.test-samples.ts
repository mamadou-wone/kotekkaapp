import dayjs from 'dayjs/esm';

import { IBeneficiary, NewBeneficiary } from './beneficiary.model';

export const sampleWithRequiredData: IBeneficiary = {
  id: 9988,
  uuid: 'bcca4756-071a-4727-b496-02708cad7211',
};

export const sampleWithPartialData: IBeneficiary = {
  id: 412,
  uuid: 'b67a6159-6c75-4a3c-a016-76211964f0ba',
  firstName: 'Andre',
  lastName: 'Turner',
  iban: 'FR3710500887776D3308863K362',
  phoneNumber: 'when or but',
  createdBy: 'porter regularly',
  lastModifiedBy: 'even',
};

export const sampleWithFullData: IBeneficiary = {
  id: 366,
  uuid: '8cc3f187-71d6-4374-aa39-21a33837a61a',
  status: 'PENDING',
  firstName: 'Joesph',
  lastName: 'Gibson',
  cin: 'moisten',
  iban: 'KW86AJQL7P188142K4517016020U7N',
  phoneNumber: 'redraw knife',
  email: 'Jennie68@gmail.com',
  walletHolder: '163ec59d-1795-4b6d-9b22-be883989ee8d',
  createdBy: 'via how',
  createdDate: dayjs('2024-12-09T11:24'),
  lastModifiedBy: 'grumpy though statement',
  lastModifiedDate: dayjs('2024-12-09T02:54'),
};

export const sampleWithNewData: NewBeneficiary = {
  uuid: 'fd937d9e-2666-428a-af2a-cb30b60704cb',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
