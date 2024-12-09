import dayjs from 'dayjs/esm';

export interface ICart {
  id: number;
  uuid?: string | null;
  walletHolder?: string | null;
  totalQuantity?: number | null;
  totalPrice?: number | null;
  currency?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedDate?: dayjs.Dayjs | null;
}

export type NewCart = Omit<ICart, 'id'> & { id: null };
