import dayjs from 'dayjs/esm';
import { ReferalStatus } from 'app/entities/enumerations/referal-status.model';

export interface IReferalInfo {
  id: number;
  uuid?: string | null;
  referalCode?: string | null;
  walletHolder?: string | null;
  refered?: string | null;
  status?: keyof typeof ReferalStatus | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
}

export type NewReferalInfo = Omit<IReferalInfo, 'id'> & { id: null };
