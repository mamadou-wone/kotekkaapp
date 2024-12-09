import dayjs from 'dayjs/esm';
import { WalletType } from 'app/entities/enumerations/wallet-type.model';
import { WalletStatus } from 'app/entities/enumerations/wallet-status.model';
import { AccountLevel } from 'app/entities/enumerations/account-level.model';

export interface IWallet {
  id: number;
  uuid?: string | null;
  type?: keyof typeof WalletType | null;
  status?: keyof typeof WalletStatus | null;
  level?: keyof typeof AccountLevel | null;
  iban?: string | null;
  currency?: string | null;
  balance?: number | null;
  balancesAsOf?: dayjs.Dayjs | null;
  externalId?: string | null;
  walletHolder?: string | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
}

export type NewWallet = Omit<IWallet, 'id'> & { id: null };
