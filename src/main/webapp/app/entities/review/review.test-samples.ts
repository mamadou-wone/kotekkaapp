import dayjs from 'dayjs/esm';

import { IReview, NewReview } from './review.model';

export const sampleWithRequiredData: IReview = {
  id: 5472,
  uuid: 'e9d33c8b-ee38-44cb-b295-380722be2991',
  product: '1236c6fa-08ba-4476-b094-0c3b47ce9b36',
  walletHolder: '416f45a1-a522-4fef-a8d0-0cb1548c2692',
  rating: 4,
};

export const sampleWithPartialData: IReview = {
  id: 14141,
  uuid: '097484c1-951f-4af0-8de8-eb6e5be6487a',
  product: '4a713d41-8814-4481-a0f3-7cc6bda65822',
  walletHolder: '66ecd823-7ff2-4f8f-bdc3-b9ff90ac1cdc',
  rating: 4,
  comment: 'remark',
};

export const sampleWithFullData: IReview = {
  id: 15372,
  uuid: 'f61b7625-bf52-4793-a158-8295403bbfc8',
  product: '7bc05a65-241f-41b3-8153-77f8a6d2e549',
  walletHolder: '7beeab0b-2b17-4393-9606-495764e7ff62',
  rating: 5,
  comment: 'sentimental',
  createdDate: dayjs('2024-12-09T02:26'),
};

export const sampleWithNewData: NewReview = {
  uuid: 'e94d335e-5f81-4d7f-99eb-add619bfe6e0',
  product: '5f508d55-5e4c-421a-a81e-8bb2a0e90528',
  walletHolder: 'e47c59b7-5e5b-4d51-af40-25969b7eb84b',
  rating: 3,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
