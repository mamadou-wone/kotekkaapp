import dayjs from 'dayjs/esm';

import { IReferalInfo, NewReferalInfo } from './referal-info.model';

export const sampleWithRequiredData: IReferalInfo = {
  id: 26947,
  uuid: 'ebba191f-68c3-4b3e-819a-26264a88bf79',
};

export const sampleWithPartialData: IReferalInfo = {
  id: 10574,
  uuid: '0eed2c06-dce2-4cc1-a806-1fb7467efac8',
  referalCode: 'mid midst task',
  status: 'REJECTED',
};

export const sampleWithFullData: IReferalInfo = {
  id: 20990,
  uuid: '20a599f8-eb25-4379-8547-fcca96b018a3',
  referalCode: 'crocodile fervently',
  walletHolder: '9bc21f80-1dea-4661-96b3-884c94413787',
  refered: 'db704878-f6f6-4884-b556-7afe1d26b4b3',
  status: 'PENDING',
  createdBy: 'vulgarise machine apud',
  createdDate: dayjs('2024-12-09T05:23'),
  lastModifiedBy: 'which camouflage apud',
  lastModifiedDate: dayjs('2024-12-09T05:05'),
};

export const sampleWithNewData: NewReferalInfo = {
  uuid: '86f1c43d-9d32-408a-b6a5-0c3dd1bd635e',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
