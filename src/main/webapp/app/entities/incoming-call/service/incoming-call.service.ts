import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IIncomingCall, NewIncomingCall } from '../incoming-call.model';

export type PartialUpdateIncomingCall = Partial<IIncomingCall> & Pick<IIncomingCall, 'id'>;

type RestOf<T extends IIncomingCall | NewIncomingCall> = Omit<T, 'createdDate' | 'responseTime'> & {
  createdDate?: string | null;
  responseTime?: string | null;
};

export type RestIncomingCall = RestOf<IIncomingCall>;

export type NewRestIncomingCall = RestOf<NewIncomingCall>;

export type PartialUpdateRestIncomingCall = RestOf<PartialUpdateIncomingCall>;

export type EntityResponseType = HttpResponse<IIncomingCall>;
export type EntityArrayResponseType = HttpResponse<IIncomingCall[]>;

@Injectable({ providedIn: 'root' })
export class IncomingCallService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/incoming-calls');

  create(incomingCall: NewIncomingCall): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(incomingCall);
    return this.http
      .post<RestIncomingCall>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(incomingCall: IIncomingCall): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(incomingCall);
    return this.http
      .put<RestIncomingCall>(`${this.resourceUrl}/${this.getIncomingCallIdentifier(incomingCall)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(incomingCall: PartialUpdateIncomingCall): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(incomingCall);
    return this.http
      .patch<RestIncomingCall>(`${this.resourceUrl}/${this.getIncomingCallIdentifier(incomingCall)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestIncomingCall>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestIncomingCall[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getIncomingCallIdentifier(incomingCall: Pick<IIncomingCall, 'id'>): number {
    return incomingCall.id;
  }

  compareIncomingCall(o1: Pick<IIncomingCall, 'id'> | null, o2: Pick<IIncomingCall, 'id'> | null): boolean {
    return o1 && o2 ? this.getIncomingCallIdentifier(o1) === this.getIncomingCallIdentifier(o2) : o1 === o2;
  }

  addIncomingCallToCollectionIfMissing<Type extends Pick<IIncomingCall, 'id'>>(
    incomingCallCollection: Type[],
    ...incomingCallsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const incomingCalls: Type[] = incomingCallsToCheck.filter(isPresent);
    if (incomingCalls.length > 0) {
      const incomingCallCollectionIdentifiers = incomingCallCollection.map(incomingCallItem =>
        this.getIncomingCallIdentifier(incomingCallItem),
      );
      const incomingCallsToAdd = incomingCalls.filter(incomingCallItem => {
        const incomingCallIdentifier = this.getIncomingCallIdentifier(incomingCallItem);
        if (incomingCallCollectionIdentifiers.includes(incomingCallIdentifier)) {
          return false;
        }
        incomingCallCollectionIdentifiers.push(incomingCallIdentifier);
        return true;
      });
      return [...incomingCallsToAdd, ...incomingCallCollection];
    }
    return incomingCallCollection;
  }

  protected convertDateFromClient<T extends IIncomingCall | NewIncomingCall | PartialUpdateIncomingCall>(incomingCall: T): RestOf<T> {
    return {
      ...incomingCall,
      createdDate: incomingCall.createdDate?.toJSON() ?? null,
      responseTime: incomingCall.responseTime?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restIncomingCall: RestIncomingCall): IIncomingCall {
    return {
      ...restIncomingCall,
      createdDate: restIncomingCall.createdDate ? dayjs(restIncomingCall.createdDate) : undefined,
      responseTime: restIncomingCall.responseTime ? dayjs(restIncomingCall.responseTime) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestIncomingCall>): HttpResponse<IIncomingCall> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestIncomingCall[]>): HttpResponse<IIncomingCall[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
