import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFailedAttempt, NewFailedAttempt } from '../failed-attempt.model';

export type PartialUpdateFailedAttempt = Partial<IFailedAttempt> & Pick<IFailedAttempt, 'id'>;

type RestOf<T extends IFailedAttempt | NewFailedAttempt> = Omit<T, 'createdDate'> & {
  createdDate?: string | null;
};

export type RestFailedAttempt = RestOf<IFailedAttempt>;

export type NewRestFailedAttempt = RestOf<NewFailedAttempt>;

export type PartialUpdateRestFailedAttempt = RestOf<PartialUpdateFailedAttempt>;

export type EntityResponseType = HttpResponse<IFailedAttempt>;
export type EntityArrayResponseType = HttpResponse<IFailedAttempt[]>;

@Injectable({ providedIn: 'root' })
export class FailedAttemptService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/failed-attempts');

  create(failedAttempt: NewFailedAttempt): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(failedAttempt);
    return this.http
      .post<RestFailedAttempt>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(failedAttempt: IFailedAttempt): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(failedAttempt);
    return this.http
      .put<RestFailedAttempt>(`${this.resourceUrl}/${this.getFailedAttemptIdentifier(failedAttempt)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(failedAttempt: PartialUpdateFailedAttempt): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(failedAttempt);
    return this.http
      .patch<RestFailedAttempt>(`${this.resourceUrl}/${this.getFailedAttemptIdentifier(failedAttempt)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestFailedAttempt>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestFailedAttempt[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getFailedAttemptIdentifier(failedAttempt: Pick<IFailedAttempt, 'id'>): number {
    return failedAttempt.id;
  }

  compareFailedAttempt(o1: Pick<IFailedAttempt, 'id'> | null, o2: Pick<IFailedAttempt, 'id'> | null): boolean {
    return o1 && o2 ? this.getFailedAttemptIdentifier(o1) === this.getFailedAttemptIdentifier(o2) : o1 === o2;
  }

  addFailedAttemptToCollectionIfMissing<Type extends Pick<IFailedAttempt, 'id'>>(
    failedAttemptCollection: Type[],
    ...failedAttemptsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const failedAttempts: Type[] = failedAttemptsToCheck.filter(isPresent);
    if (failedAttempts.length > 0) {
      const failedAttemptCollectionIdentifiers = failedAttemptCollection.map(failedAttemptItem =>
        this.getFailedAttemptIdentifier(failedAttemptItem),
      );
      const failedAttemptsToAdd = failedAttempts.filter(failedAttemptItem => {
        const failedAttemptIdentifier = this.getFailedAttemptIdentifier(failedAttemptItem);
        if (failedAttemptCollectionIdentifiers.includes(failedAttemptIdentifier)) {
          return false;
        }
        failedAttemptCollectionIdentifiers.push(failedAttemptIdentifier);
        return true;
      });
      return [...failedAttemptsToAdd, ...failedAttemptCollection];
    }
    return failedAttemptCollection;
  }

  protected convertDateFromClient<T extends IFailedAttempt | NewFailedAttempt | PartialUpdateFailedAttempt>(failedAttempt: T): RestOf<T> {
    return {
      ...failedAttempt,
      createdDate: failedAttempt.createdDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restFailedAttempt: RestFailedAttempt): IFailedAttempt {
    return {
      ...restFailedAttempt,
      createdDate: restFailedAttempt.createdDate ? dayjs(restFailedAttempt.createdDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestFailedAttempt>): HttpResponse<IFailedAttempt> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestFailedAttempt[]>): HttpResponse<IFailedAttempt[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
