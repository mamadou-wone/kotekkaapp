import dayjs from 'dayjs/esm';

export interface ICin {
  id: number;
  cinId?: string | null;
  validityDate?: dayjs.Dayjs | null;
  birthDate?: dayjs.Dayjs | null;
  birthPlace?: string | null;
  firstName?: string | null;
  lastName?: string | null;
  birthCity?: string | null;
  fatherName?: string | null;
  nationality?: string | null;
  nationalityCode?: string | null;
  issuingCountry?: string | null;
  issuingCountryCode?: string | null;
  motherName?: string | null;
  civilRegister?: string | null;
  sex?: string | null;
  address?: string | null;
  birthCityCode?: string | null;
  walletHolder?: string | null;
  createdDate?: dayjs.Dayjs | null;
}

export type NewCin = Omit<ICin, 'id'> & { id: null };
