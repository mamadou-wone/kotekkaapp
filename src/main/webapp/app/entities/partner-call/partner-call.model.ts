import dayjs from 'dayjs/esm';
import { Partner } from 'app/entities/enumerations/partner.model';
import { HttpMethod } from 'app/entities/enumerations/http-method.model';

export interface IPartnerCall {
  id: number;
  partner?: keyof typeof Partner | null;
  api?: string | null;
  method?: keyof typeof HttpMethod | null;
  requestHeaders?: string | null;
  requestBody?: string | null;
  requestTime?: dayjs.Dayjs | null;
  responseStatusCode?: number | null;
  responseHeaders?: string | null;
  responseBody?: string | null;
  responseTime?: dayjs.Dayjs | null;
  correlationId?: string | null;
  queryParam?: string | null;
}

export type NewPartnerCall = Omit<IPartnerCall, 'id'> & { id: null };
