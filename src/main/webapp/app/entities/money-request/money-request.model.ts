import dayjs from 'dayjs/esm';
import { RequestStatus } from 'app/entities/enumerations/request-status.model';

export interface IMoneyRequest {
  id: number;
  uuid?: string | null;
  status?: keyof typeof RequestStatus | null;
  otherHolder?: string | null;
  amount?: number | null;
  description?: string | null;
  currency?: string | null;
  walletHolder?: string | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
}

export type NewMoneyRequest = Omit<IMoneyRequest, 'id'> & { id: null };
