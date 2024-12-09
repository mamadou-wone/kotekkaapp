import dayjs from 'dayjs/esm';
import { AccountType } from 'app/entities/enumerations/account-type.model';
import { DefaultStatus } from 'app/entities/enumerations/default-status.model';

export interface IServiceClient {
  id: number;
  clientId?: string | null;
  type?: keyof typeof AccountType | null;
  apiKey?: string | null;
  status?: keyof typeof DefaultStatus | null;
  note?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedDate?: dayjs.Dayjs | null;
}

export type NewServiceClient = Omit<IServiceClient, 'id'> & { id: null };
