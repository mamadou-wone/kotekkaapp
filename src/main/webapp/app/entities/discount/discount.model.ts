import dayjs from 'dayjs/esm';
import { DiscountType } from 'app/entities/enumerations/discount-type.model';
import { DefaultStatus } from 'app/entities/enumerations/default-status.model';

export interface IDiscount {
  id: number;
  uuid?: string | null;
  name?: string | null;
  type?: keyof typeof DiscountType | null;
  value?: number | null;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  status?: keyof typeof DefaultStatus | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
}

export type NewDiscount = Omit<IDiscount, 'id'> & { id: null };
