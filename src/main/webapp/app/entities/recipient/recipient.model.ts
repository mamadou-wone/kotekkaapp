import dayjs from 'dayjs/esm';
import { DefaultStatus } from 'app/entities/enumerations/default-status.model';

export interface IRecipient {
  id: number;
  uuid?: string | null;
  status?: keyof typeof DefaultStatus | null;
  firstName?: string | null;
  lastName?: string | null;
  phoneNumber?: string | null;
  walletHolder?: string | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
}

export type NewRecipient = Omit<IRecipient, 'id'> & { id: null };
