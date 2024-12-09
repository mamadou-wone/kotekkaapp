import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDiscount, NewDiscount } from '../discount.model';

export type PartialUpdateDiscount = Partial<IDiscount> & Pick<IDiscount, 'id'>;

type RestOf<T extends IDiscount | NewDiscount> = Omit<T, 'startDate' | 'endDate' | 'createdDate' | 'lastModifiedDate'> & {
  startDate?: string | null;
  endDate?: string | null;
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

export type RestDiscount = RestOf<IDiscount>;

export type NewRestDiscount = RestOf<NewDiscount>;

export type PartialUpdateRestDiscount = RestOf<PartialUpdateDiscount>;

export type EntityResponseType = HttpResponse<IDiscount>;
export type EntityArrayResponseType = HttpResponse<IDiscount[]>;

@Injectable({ providedIn: 'root' })
export class DiscountService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/discounts');

  create(discount: NewDiscount): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(discount);
    return this.http
      .post<RestDiscount>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(discount: IDiscount): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(discount);
    return this.http
      .put<RestDiscount>(`${this.resourceUrl}/${this.getDiscountIdentifier(discount)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(discount: PartialUpdateDiscount): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(discount);
    return this.http
      .patch<RestDiscount>(`${this.resourceUrl}/${this.getDiscountIdentifier(discount)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestDiscount>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestDiscount[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getDiscountIdentifier(discount: Pick<IDiscount, 'id'>): number {
    return discount.id;
  }

  compareDiscount(o1: Pick<IDiscount, 'id'> | null, o2: Pick<IDiscount, 'id'> | null): boolean {
    return o1 && o2 ? this.getDiscountIdentifier(o1) === this.getDiscountIdentifier(o2) : o1 === o2;
  }

  addDiscountToCollectionIfMissing<Type extends Pick<IDiscount, 'id'>>(
    discountCollection: Type[],
    ...discountsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const discounts: Type[] = discountsToCheck.filter(isPresent);
    if (discounts.length > 0) {
      const discountCollectionIdentifiers = discountCollection.map(discountItem => this.getDiscountIdentifier(discountItem));
      const discountsToAdd = discounts.filter(discountItem => {
        const discountIdentifier = this.getDiscountIdentifier(discountItem);
        if (discountCollectionIdentifiers.includes(discountIdentifier)) {
          return false;
        }
        discountCollectionIdentifiers.push(discountIdentifier);
        return true;
      });
      return [...discountsToAdd, ...discountCollection];
    }
    return discountCollection;
  }

  protected convertDateFromClient<T extends IDiscount | NewDiscount | PartialUpdateDiscount>(discount: T): RestOf<T> {
    return {
      ...discount,
      startDate: discount.startDate?.toJSON() ?? null,
      endDate: discount.endDate?.toJSON() ?? null,
      createdDate: discount.createdDate?.toJSON() ?? null,
      lastModifiedDate: discount.lastModifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restDiscount: RestDiscount): IDiscount {
    return {
      ...restDiscount,
      startDate: restDiscount.startDate ? dayjs(restDiscount.startDate) : undefined,
      endDate: restDiscount.endDate ? dayjs(restDiscount.endDate) : undefined,
      createdDate: restDiscount.createdDate ? dayjs(restDiscount.createdDate) : undefined,
      lastModifiedDate: restDiscount.lastModifiedDate ? dayjs(restDiscount.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestDiscount>): HttpResponse<IDiscount> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestDiscount[]>): HttpResponse<IDiscount[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
