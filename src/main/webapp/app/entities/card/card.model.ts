import dayjs from 'dayjs/esm';
import { DefaultStatus } from 'app/entities/enumerations/default-status.model';

export interface ICard {
  id: number;
  uuid?: string | null;
  status?: keyof typeof DefaultStatus | null;
  label?: string | null;
  maskedPan?: string | null;
  cardHolderName?: string | null;
  token?: string | null;
  expiryYear?: string | null;
  expiryMonth?: string | null;
  rnd?: string | null;
  hash?: string | null;
  walletHolder?: string | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
}

export type NewCard = Omit<ICard, 'id'> & { id: null };
