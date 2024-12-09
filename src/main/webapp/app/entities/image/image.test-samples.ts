import dayjs from 'dayjs/esm';

import { IImage, NewImage } from './image.model';

export const sampleWithRequiredData: IImage = {
  id: 4976,
  uuid: '1261571f-5d61-4ac2-adbe-6ad6de91fd6d',
};

export const sampleWithPartialData: IImage = {
  id: 21079,
  uuid: '0c8b3303-6bb0-43bd-97a1-bff508b1805f',
  name: 'recovery ack',
  file: '../fake-data/blob/hipster.png',
  fileContentType: 'unknown',
  walletHolder: '35df9180-c502-48a1-99fd-f22a93d936cf',
  createdBy: 'gosh sock',
  createdDate: dayjs('2024-12-09T09:39'),
  lastModifiedBy: 'upset hovel ready',
  lastModifiedDate: dayjs('2024-12-08T20:51'),
};

export const sampleWithFullData: IImage = {
  id: 6469,
  uuid: 'a2b4c94d-729c-4ee5-a4bd-d214b5b4cfa4',
  name: 'misjudge petticoat',
  file: '../fake-data/blob/hipster.png',
  fileContentType: 'unknown',
  walletHolder: '8c6d0d9b-65d1-4f5e-9a01-9b20b7849be2',
  createdBy: 'randomize',
  createdDate: dayjs('2024-12-09T05:50'),
  lastModifiedBy: 'steeple upliftingly',
  lastModifiedDate: dayjs('2024-12-08T19:40'),
};

export const sampleWithNewData: NewImage = {
  uuid: '7b7b5b97-f54f-4201-87de-03bf58620487',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
