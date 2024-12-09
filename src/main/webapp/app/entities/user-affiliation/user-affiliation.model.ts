import dayjs from 'dayjs/esm';

export interface IUserAffiliation {
  id: number;
  walletHolder?: string | null;
  affiliation?: string | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
}

export type NewUserAffiliation = Omit<IUserAffiliation, 'id'> & { id: null };
