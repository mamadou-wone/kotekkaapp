import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMoneyRequest, NewMoneyRequest } from '../money-request.model';

export type PartialUpdateMoneyRequest = Partial<IMoneyRequest> & Pick<IMoneyRequest, 'id'>;

type RestOf<T extends IMoneyRequest | NewMoneyRequest> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

export type RestMoneyRequest = RestOf<IMoneyRequest>;

export type NewRestMoneyRequest = RestOf<NewMoneyRequest>;

export type PartialUpdateRestMoneyRequest = RestOf<PartialUpdateMoneyRequest>;

export type EntityResponseType = HttpResponse<IMoneyRequest>;
export type EntityArrayResponseType = HttpResponse<IMoneyRequest[]>;

@Injectable({ providedIn: 'root' })
export class MoneyRequestService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/money-requests');

  create(moneyRequest: NewMoneyRequest): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(moneyRequest);
    return this.http
      .post<RestMoneyRequest>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(moneyRequest: IMoneyRequest): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(moneyRequest);
    return this.http
      .put<RestMoneyRequest>(`${this.resourceUrl}/${this.getMoneyRequestIdentifier(moneyRequest)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(moneyRequest: PartialUpdateMoneyRequest): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(moneyRequest);
    return this.http
      .patch<RestMoneyRequest>(`${this.resourceUrl}/${this.getMoneyRequestIdentifier(moneyRequest)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestMoneyRequest>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestMoneyRequest[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getMoneyRequestIdentifier(moneyRequest: Pick<IMoneyRequest, 'id'>): number {
    return moneyRequest.id;
  }

  compareMoneyRequest(o1: Pick<IMoneyRequest, 'id'> | null, o2: Pick<IMoneyRequest, 'id'> | null): boolean {
    return o1 && o2 ? this.getMoneyRequestIdentifier(o1) === this.getMoneyRequestIdentifier(o2) : o1 === o2;
  }

  addMoneyRequestToCollectionIfMissing<Type extends Pick<IMoneyRequest, 'id'>>(
    moneyRequestCollection: Type[],
    ...moneyRequestsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const moneyRequests: Type[] = moneyRequestsToCheck.filter(isPresent);
    if (moneyRequests.length > 0) {
      const moneyRequestCollectionIdentifiers = moneyRequestCollection.map(moneyRequestItem =>
        this.getMoneyRequestIdentifier(moneyRequestItem),
      );
      const moneyRequestsToAdd = moneyRequests.filter(moneyRequestItem => {
        const moneyRequestIdentifier = this.getMoneyRequestIdentifier(moneyRequestItem);
        if (moneyRequestCollectionIdentifiers.includes(moneyRequestIdentifier)) {
          return false;
        }
        moneyRequestCollectionIdentifiers.push(moneyRequestIdentifier);
        return true;
      });
      return [...moneyRequestsToAdd, ...moneyRequestCollection];
    }
    return moneyRequestCollection;
  }

  protected convertDateFromClient<T extends IMoneyRequest | NewMoneyRequest | PartialUpdateMoneyRequest>(moneyRequest: T): RestOf<T> {
    return {
      ...moneyRequest,
      createdDate: moneyRequest.createdDate?.toJSON() ?? null,
      lastModifiedDate: moneyRequest.lastModifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restMoneyRequest: RestMoneyRequest): IMoneyRequest {
    return {
      ...restMoneyRequest,
      createdDate: restMoneyRequest.createdDate ? dayjs(restMoneyRequest.createdDate) : undefined,
      lastModifiedDate: restMoneyRequest.lastModifiedDate ? dayjs(restMoneyRequest.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestMoneyRequest>): HttpResponse<IMoneyRequest> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestMoneyRequest[]>): HttpResponse<IMoneyRequest[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
