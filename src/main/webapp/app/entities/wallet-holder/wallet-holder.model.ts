import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';
import { AccountType } from 'app/entities/enumerations/account-type.model';
import { AccountStatus } from 'app/entities/enumerations/account-status.model';
import { Network } from 'app/entities/enumerations/network.model';
import { OnboardingStatus } from 'app/entities/enumerations/onboarding-status.model';
import { Sex } from 'app/entities/enumerations/sex.model';
import { LoginStatus } from 'app/entities/enumerations/login-status.model';

export interface IWalletHolder {
  id: number;
  uuid?: string | null;
  type?: keyof typeof AccountType | null;
  status?: keyof typeof AccountStatus | null;
  phoneNumber?: string | null;
  network?: keyof typeof Network | null;
  tag?: string | null;
  firstName?: string | null;
  lastName?: string | null;
  address?: string | null;
  city?: string | null;
  country?: string | null;
  postalCode?: string | null;
  onboarding?: keyof typeof OnboardingStatus | null;
  externalId?: string | null;
  email?: string | null;
  dateOfBirth?: dayjs.Dayjs | null;
  sex?: keyof typeof Sex | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
  loginStatus?: keyof typeof LoginStatus | null;
  user?: Pick<IUser, 'id' | 'login'> | null;
}

export type NewWalletHolder = Omit<IWalletHolder, 'id'> & { id: null };
