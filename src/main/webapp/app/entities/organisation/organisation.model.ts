import dayjs from 'dayjs/esm';
import { OrgType } from 'app/entities/enumerations/org-type.model';

export interface IOrganisation {
  id: number;
  name?: string | null;
  type?: keyof typeof OrgType | null;
  parent?: string | null;
  location?: string | null;
  headcount?: number | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
}

export type NewOrganisation = Omit<IOrganisation, 'id'> & { id: null };
