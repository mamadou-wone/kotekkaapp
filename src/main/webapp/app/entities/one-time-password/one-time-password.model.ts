import dayjs from 'dayjs/esm';
import { OtpStatus } from 'app/entities/enumerations/otp-status.model';

export interface IOneTimePassword {
  id: number;
  uuid?: string | null;
  user?: string | null;
  code?: string | null;
  status?: keyof typeof OtpStatus | null;
  expiry?: dayjs.Dayjs | null;
  createdDate?: dayjs.Dayjs | null;
}

export type NewOneTimePassword = Omit<IOneTimePassword, 'id'> & { id: null };
