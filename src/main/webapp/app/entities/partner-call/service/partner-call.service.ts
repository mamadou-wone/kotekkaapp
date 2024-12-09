import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPartnerCall, NewPartnerCall } from '../partner-call.model';

export type PartialUpdatePartnerCall = Partial<IPartnerCall> & Pick<IPartnerCall, 'id'>;

type RestOf<T extends IPartnerCall | NewPartnerCall> = Omit<T, 'requestTime' | 'responseTime'> & {
  requestTime?: string | null;
  responseTime?: string | null;
};

export type RestPartnerCall = RestOf<IPartnerCall>;

export type NewRestPartnerCall = RestOf<NewPartnerCall>;

export type PartialUpdateRestPartnerCall = RestOf<PartialUpdatePartnerCall>;

export type EntityResponseType = HttpResponse<IPartnerCall>;
export type EntityArrayResponseType = HttpResponse<IPartnerCall[]>;

@Injectable({ providedIn: 'root' })
export class PartnerCallService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/partner-calls');

  create(partnerCall: NewPartnerCall): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(partnerCall);
    return this.http
      .post<RestPartnerCall>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(partnerCall: IPartnerCall): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(partnerCall);
    return this.http
      .put<RestPartnerCall>(`${this.resourceUrl}/${this.getPartnerCallIdentifier(partnerCall)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(partnerCall: PartialUpdatePartnerCall): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(partnerCall);
    return this.http
      .patch<RestPartnerCall>(`${this.resourceUrl}/${this.getPartnerCallIdentifier(partnerCall)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestPartnerCall>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestPartnerCall[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPartnerCallIdentifier(partnerCall: Pick<IPartnerCall, 'id'>): number {
    return partnerCall.id;
  }

  comparePartnerCall(o1: Pick<IPartnerCall, 'id'> | null, o2: Pick<IPartnerCall, 'id'> | null): boolean {
    return o1 && o2 ? this.getPartnerCallIdentifier(o1) === this.getPartnerCallIdentifier(o2) : o1 === o2;
  }

  addPartnerCallToCollectionIfMissing<Type extends Pick<IPartnerCall, 'id'>>(
    partnerCallCollection: Type[],
    ...partnerCallsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const partnerCalls: Type[] = partnerCallsToCheck.filter(isPresent);
    if (partnerCalls.length > 0) {
      const partnerCallCollectionIdentifiers = partnerCallCollection.map(partnerCallItem => this.getPartnerCallIdentifier(partnerCallItem));
      const partnerCallsToAdd = partnerCalls.filter(partnerCallItem => {
        const partnerCallIdentifier = this.getPartnerCallIdentifier(partnerCallItem);
        if (partnerCallCollectionIdentifiers.includes(partnerCallIdentifier)) {
          return false;
        }
        partnerCallCollectionIdentifiers.push(partnerCallIdentifier);
        return true;
      });
      return [...partnerCallsToAdd, ...partnerCallCollection];
    }
    return partnerCallCollection;
  }

  protected convertDateFromClient<T extends IPartnerCall | NewPartnerCall | PartialUpdatePartnerCall>(partnerCall: T): RestOf<T> {
    return {
      ...partnerCall,
      requestTime: partnerCall.requestTime?.toJSON() ?? null,
      responseTime: partnerCall.responseTime?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restPartnerCall: RestPartnerCall): IPartnerCall {
    return {
      ...restPartnerCall,
      requestTime: restPartnerCall.requestTime ? dayjs(restPartnerCall.requestTime) : undefined,
      responseTime: restPartnerCall.responseTime ? dayjs(restPartnerCall.responseTime) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestPartnerCall>): HttpResponse<IPartnerCall> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestPartnerCall[]>): HttpResponse<IPartnerCall[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
