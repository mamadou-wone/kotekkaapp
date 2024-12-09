import dayjs from 'dayjs/esm';
import { OrderStatus } from 'app/entities/enumerations/order-status.model';

export interface IOrder {
  id: number;
  uuid?: string | null;
  walletHolder?: string | null;
  status?: keyof typeof OrderStatus | null;
  totalPrice?: number | null;
  currency?: string | null;
  orderDate?: dayjs.Dayjs | null;
  paymentMethod?: string | null;
  shippingAddress?: string | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
}

export type NewOrder = Omit<IOrder, 'id'> & { id: null };
