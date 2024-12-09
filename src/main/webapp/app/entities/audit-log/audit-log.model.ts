import dayjs from 'dayjs/esm';

export interface IAuditLog {
  id: number;
  uuid?: string | null;
  entityName?: string | null;
  entityId?: string | null;
  action?: string | null;
  performedBy?: string | null;
  performedDate?: dayjs.Dayjs | null;
  details?: string | null;
}

export type NewAuditLog = Omit<IAuditLog, 'id'> & { id: null };
