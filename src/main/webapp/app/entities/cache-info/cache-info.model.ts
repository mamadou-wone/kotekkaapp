import dayjs from 'dayjs/esm';

export interface ICacheInfo {
  id: number;
  walletHolder?: string | null;
  key?: string | null;
  value?: string | null;
  createdDate?: dayjs.Dayjs | null;
}

export type NewCacheInfo = Omit<ICacheInfo, 'id'> & { id: null };
