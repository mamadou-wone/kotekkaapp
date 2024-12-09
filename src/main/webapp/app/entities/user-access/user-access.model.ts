import dayjs from 'dayjs/esm';

export interface IUserAccess {
  id: number;
  login?: string | null;
  ipAddress?: string | null;
  device?: string | null;
  createdDate?: dayjs.Dayjs | null;
}

export type NewUserAccess = Omit<IUserAccess, 'id'> & { id: null };
