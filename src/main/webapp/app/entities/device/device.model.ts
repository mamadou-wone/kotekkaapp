import dayjs from 'dayjs/esm';

export interface IDevice {
  id: number;
  uuid?: string | null;
  deviceUuid?: string | null;
  type?: string | null;
  manufacturer?: string | null;
  model?: string | null;
  os?: string | null;
  appVersion?: string | null;
  inactive?: boolean | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
}

export type NewDevice = Omit<IDevice, 'id'> & { id: null };
