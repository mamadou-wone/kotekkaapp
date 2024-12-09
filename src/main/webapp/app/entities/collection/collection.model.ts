import { IProduct } from 'app/entities/product/product.model';

export interface ICollection {
  id: number;
  name?: string | null;
  products?: IProduct[] | null;
}

export type NewCollection = Omit<ICollection, 'id'> & { id: null };
