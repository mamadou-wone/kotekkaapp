import dayjs from 'dayjs/esm';
import { App } from 'app/entities/enumerations/app.model';
import { Action } from 'app/entities/enumerations/action.model';

export interface IFailedAttempt {
  id: number;
  login?: string | null;
  ipAddress?: string | null;
  isAfterLock?: boolean | null;
  app?: keyof typeof App | null;
  action?: keyof typeof Action | null;
  device?: string | null;
  createdDate?: dayjs.Dayjs | null;
  reason?: string | null;
}

export type NewFailedAttempt = Omit<IFailedAttempt, 'id'> & { id: null };
