import dayjs from 'dayjs/esm';

import { ICard, NewCard } from './card.model';

export const sampleWithRequiredData: ICard = {
  id: 28510,
  uuid: '37bacba2-47a3-43e9-8349-8c7021aa5c41',
};

export const sampleWithPartialData: ICard = {
  id: 18810,
  uuid: '24fde053-3dfa-4eeb-b2f4-b3c512dd22ef',
  cardHolderName: 'hm',
  token: 'fortunately wearily',
  walletHolder: '0802ddb8-ec82-4a84-b598-1566ee3fb68a',
  createdBy: 'implode omelet',
  lastModifiedBy: 'multicolored yarmulke farm',
  lastModifiedDate: dayjs('2024-12-09T00:42'),
};

export const sampleWithFullData: ICard = {
  id: 12076,
  uuid: '18dc8c76-d371-49e4-9659-889c4e5baf1e',
  status: 'ACTIVE',
  label: 'alliance',
  maskedPan: 'surprise whether',
  cardHolderName: 'pillory likewise concerning',
  token: 'papa',
  expiryYear: 'kn',
  expiryMonth: 'ap',
  rnd: 'disadvantage',
  hash: 'brood injunction',
  walletHolder: '14c4cd29-0efe-4652-9959-aa77a505c6cd',
  createdBy: 'modulo kielbasa',
  createdDate: dayjs('2024-12-08T14:03'),
  lastModifiedBy: 'paintwork huzzah',
  lastModifiedDate: dayjs('2024-12-08T21:34'),
};

export const sampleWithNewData: NewCard = {
  uuid: '612d492d-d73b-47de-9331-1a334cad86f2',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
