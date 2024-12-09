import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFailedAttemptHistory, NewFailedAttemptHistory } from '../failed-attempt-history.model';

export type PartialUpdateFailedAttemptHistory = Partial<IFailedAttemptHistory> & Pick<IFailedAttemptHistory, 'id'>;

type RestOf<T extends IFailedAttemptHistory | NewFailedAttemptHistory> = Omit<T, 'createdDate'> & {
  createdDate?: string | null;
};

export type RestFailedAttemptHistory = RestOf<IFailedAttemptHistory>;

export type NewRestFailedAttemptHistory = RestOf<NewFailedAttemptHistory>;

export type PartialUpdateRestFailedAttemptHistory = RestOf<PartialUpdateFailedAttemptHistory>;

export type EntityResponseType = HttpResponse<IFailedAttemptHistory>;
export type EntityArrayResponseType = HttpResponse<IFailedAttemptHistory[]>;

@Injectable({ providedIn: 'root' })
export class FailedAttemptHistoryService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/failed-attempt-histories');

  create(failedAttemptHistory: NewFailedAttemptHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(failedAttemptHistory);
    return this.http
      .post<RestFailedAttemptHistory>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(failedAttemptHistory: IFailedAttemptHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(failedAttemptHistory);
    return this.http
      .put<RestFailedAttemptHistory>(`${this.resourceUrl}/${this.getFailedAttemptHistoryIdentifier(failedAttemptHistory)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(failedAttemptHistory: PartialUpdateFailedAttemptHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(failedAttemptHistory);
    return this.http
      .patch<RestFailedAttemptHistory>(`${this.resourceUrl}/${this.getFailedAttemptHistoryIdentifier(failedAttemptHistory)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestFailedAttemptHistory>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestFailedAttemptHistory[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getFailedAttemptHistoryIdentifier(failedAttemptHistory: Pick<IFailedAttemptHistory, 'id'>): number {
    return failedAttemptHistory.id;
  }

  compareFailedAttemptHistory(o1: Pick<IFailedAttemptHistory, 'id'> | null, o2: Pick<IFailedAttemptHistory, 'id'> | null): boolean {
    return o1 && o2 ? this.getFailedAttemptHistoryIdentifier(o1) === this.getFailedAttemptHistoryIdentifier(o2) : o1 === o2;
  }

  addFailedAttemptHistoryToCollectionIfMissing<Type extends Pick<IFailedAttemptHistory, 'id'>>(
    failedAttemptHistoryCollection: Type[],
    ...failedAttemptHistoriesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const failedAttemptHistories: Type[] = failedAttemptHistoriesToCheck.filter(isPresent);
    if (failedAttemptHistories.length > 0) {
      const failedAttemptHistoryCollectionIdentifiers = failedAttemptHistoryCollection.map(failedAttemptHistoryItem =>
        this.getFailedAttemptHistoryIdentifier(failedAttemptHistoryItem),
      );
      const failedAttemptHistoriesToAdd = failedAttemptHistories.filter(failedAttemptHistoryItem => {
        const failedAttemptHistoryIdentifier = this.getFailedAttemptHistoryIdentifier(failedAttemptHistoryItem);
        if (failedAttemptHistoryCollectionIdentifiers.includes(failedAttemptHistoryIdentifier)) {
          return false;
        }
        failedAttemptHistoryCollectionIdentifiers.push(failedAttemptHistoryIdentifier);
        return true;
      });
      return [...failedAttemptHistoriesToAdd, ...failedAttemptHistoryCollection];
    }
    return failedAttemptHistoryCollection;
  }

  protected convertDateFromClient<T extends IFailedAttemptHistory | NewFailedAttemptHistory | PartialUpdateFailedAttemptHistory>(
    failedAttemptHistory: T,
  ): RestOf<T> {
    return {
      ...failedAttemptHistory,
      createdDate: failedAttemptHistory.createdDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restFailedAttemptHistory: RestFailedAttemptHistory): IFailedAttemptHistory {
    return {
      ...restFailedAttemptHistory,
      createdDate: restFailedAttemptHistory.createdDate ? dayjs(restFailedAttemptHistory.createdDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestFailedAttemptHistory>): HttpResponse<IFailedAttemptHistory> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestFailedAttemptHistory[]>): HttpResponse<IFailedAttemptHistory[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
