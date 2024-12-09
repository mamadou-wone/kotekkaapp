import dayjs from 'dayjs/esm';
import { DefaultStatus } from 'app/entities/enumerations/default-status.model';

export interface IBeneficiary {
  id: number;
  uuid?: string | null;
  status?: keyof typeof DefaultStatus | null;
  firstName?: string | null;
  lastName?: string | null;
  cin?: string | null;
  iban?: string | null;
  phoneNumber?: string | null;
  email?: string | null;
  walletHolder?: string | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
}

export type NewBeneficiary = Omit<IBeneficiary, 'id'> & { id: null };
