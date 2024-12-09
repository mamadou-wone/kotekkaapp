import dayjs from 'dayjs/esm';

import { INotification, NewNotification } from './notification.model';

export const sampleWithRequiredData: INotification = {
  id: 8798,
};

export const sampleWithPartialData: INotification = {
  id: 9895,
  uuid: '9d9f673a-d2be-4f64-909c-f371ed46deec',
  walletHolder: '62639be8-1dad-434f-96ff-996aa81d1ecc',
  heading: 'intensely aware far',
  content: 'excepting',
  data: 'righteously noted',
};

export const sampleWithFullData: INotification = {
  id: 24389,
  uuid: 'f1686e8c-1b21-4327-880a-e87cff2e84a2',
  walletHolder: '0593c94f-746a-4d36-9098-dd1e0ec2bdba',
  heading: 'ah masculinize amidst',
  status: 'REQUESTED',
  content: 'scoop hopelessly woot',
  data: 'destock',
  language: 'ya',
  createdDate: dayjs('2024-12-09T07:42'),
};

export const sampleWithNewData: NewNotification = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
