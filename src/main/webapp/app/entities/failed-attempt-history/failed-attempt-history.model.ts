import dayjs from 'dayjs/esm';
import { App } from 'app/entities/enumerations/app.model';
import { Action } from 'app/entities/enumerations/action.model';

export interface IFailedAttemptHistory {
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

export type NewFailedAttemptHistory = Omit<IFailedAttemptHistory, 'id'> & { id: null };
