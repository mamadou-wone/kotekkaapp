export interface ICartItem {
  id: number;
  uuid?: string | null;
  cart?: string | null;
  product?: string | null;
  quantity?: number | null;
  price?: number | null;
  totalPrice?: number | null;
}

export type NewCartItem = Omit<ICartItem, 'id'> & { id: null };
