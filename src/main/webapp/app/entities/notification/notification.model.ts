import dayjs from 'dayjs/esm';
import { NotificationStatus } from 'app/entities/enumerations/notification-status.model';

export interface INotification {
  id: number;
  uuid?: string | null;
  walletHolder?: string | null;
  heading?: string | null;
  status?: keyof typeof NotificationStatus | null;
  content?: string | null;
  data?: string | null;
  language?: string | null;
  createdDate?: dayjs.Dayjs | null;
}

export type NewNotification = Omit<INotification, 'id'> & { id: null };
