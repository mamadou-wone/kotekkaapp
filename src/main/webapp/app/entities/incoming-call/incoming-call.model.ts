import dayjs from 'dayjs/esm';
import { Partner } from 'app/entities/enumerations/partner.model';
import { HttpMethod } from 'app/entities/enumerations/http-method.model';

export interface IIncomingCall {
  id: number;
  partner?: keyof typeof Partner | null;
  api?: string | null;
  method?: keyof typeof HttpMethod | null;
  requestHeaders?: string | null;
  requestBody?: string | null;
  createdDate?: dayjs.Dayjs | null;
  responseStatusCode?: number | null;
  responseTime?: dayjs.Dayjs | null;
}

export type NewIncomingCall = Omit<IIncomingCall, 'id'> & { id: null };
