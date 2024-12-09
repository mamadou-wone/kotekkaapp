import dayjs from 'dayjs/esm';

export interface IReview {
  id: number;
  uuid?: string | null;
  product?: string | null;
  walletHolder?: string | null;
  rating?: number | null;
  comment?: string | null;
  createdDate?: dayjs.Dayjs | null;
}

export type NewReview = Omit<IReview, 'id'> & { id: null };
