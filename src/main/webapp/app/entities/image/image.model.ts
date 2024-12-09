import dayjs from 'dayjs/esm';

export interface IImage {
  id: number;
  uuid?: string | null;
  name?: string | null;
  file?: string | null;
  fileContentType?: string | null;
  walletHolder?: string | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
}

export type NewImage = Omit<IImage, 'id'> & { id: null };
