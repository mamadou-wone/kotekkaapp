import dayjs from 'dayjs/esm';
import { TransactionType } from 'app/entities/enumerations/transaction-type.model';
import { TransactionStatus } from 'app/entities/enumerations/transaction-status.model';
import { Direction } from 'app/entities/enumerations/direction.model';
import { CounterpartyType } from 'app/entities/enumerations/counterparty-type.model';

export interface ITransaction {
  id: number;
  uuid?: string | null;
  type?: keyof typeof TransactionType | null;
  status?: keyof typeof TransactionStatus | null;
  direction?: keyof typeof Direction | null;
  amount?: number | null;
  description?: string | null;
  fee?: number | null;
  commission?: number | null;
  currency?: string | null;
  counterpartyType?: keyof typeof CounterpartyType | null;
  counterpartyId?: string | null;
  entryDate?: dayjs.Dayjs | null;
  effectiveDate?: dayjs.Dayjs | null;
  startTime?: dayjs.Dayjs | null;
  endTime?: dayjs.Dayjs | null;
  parent?: string | null;
  reference?: string | null;
  externalId?: string | null;
  partnerToken?: string | null;
  wallet?: string | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
}

export type NewTransaction = Omit<ITransaction, 'id'> & { id: null };
