import dayjs from 'dayjs/esm';

export interface IFeatureFlag {
  id: number;
  name?: string | null;
  enabled?: boolean | null;
  description?: string | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  updatedBy?: string | null;
  updatedDate?: dayjs.Dayjs | null;
}

export type NewFeatureFlag = Omit<IFeatureFlag, 'id'> & { id: null };
