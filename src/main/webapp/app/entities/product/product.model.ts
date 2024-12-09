import { ICategory } from 'app/entities/category/category.model';
import { ICollection } from 'app/entities/collection/collection.model';
import { ProductStatus } from 'app/entities/enumerations/product-status.model';

export interface IProduct {
  id: number;
  uuid?: string | null;
  walletHolder?: string | null;
  title?: string | null;
  description?: string | null;
  status?: keyof typeof ProductStatus | null;
  media?: string | null;
  mediaContentType?: string | null;
  price?: number | null;
  compareAtPrice?: number | null;
  costPerItem?: number | null;
  profit?: number | null;
  margin?: number | null;
  inventoryQuantity?: number | null;
  inventoryLocation?: string | null;
  trackQuantity?: boolean | null;
  category?: ICategory | null;
  collections?: ICollection[] | null;
}

export type NewProduct = Omit<IProduct, 'id'> & { id: null };
