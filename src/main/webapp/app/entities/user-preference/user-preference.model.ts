import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';
import { App } from 'app/entities/enumerations/app.model';

export interface IUserPreference {
  id: number;
  app?: keyof typeof App | null;
  name?: string | null;
  setting?: string | null;
  createdDate?: dayjs.Dayjs | null;
  user?: Pick<IUser, 'id' | 'login'> | null;
}

export type NewUserPreference = Omit<IUserPreference, 'id'> & { id: null };
