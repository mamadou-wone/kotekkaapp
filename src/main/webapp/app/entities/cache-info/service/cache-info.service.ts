import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICacheInfo, NewCacheInfo } from '../cache-info.model';

export type PartialUpdateCacheInfo = Partial<ICacheInfo> & Pick<ICacheInfo, 'id'>;

type RestOf<T extends ICacheInfo | NewCacheInfo> = Omit<T, 'createdDate'> & {
  createdDate?: string | null;
};

export type RestCacheInfo = RestOf<ICacheInfo>;

export type NewRestCacheInfo = RestOf<NewCacheInfo>;

export type PartialUpdateRestCacheInfo = RestOf<PartialUpdateCacheInfo>;

export type EntityResponseType = HttpResponse<ICacheInfo>;
export type EntityArrayResponseType = HttpResponse<ICacheInfo[]>;

@Injectable({ providedIn: 'root' })
export class CacheInfoService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/cache-infos');

  create(cacheInfo: NewCacheInfo): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cacheInfo);
    return this.http
      .post<RestCacheInfo>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(cacheInfo: ICacheInfo): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cacheInfo);
    return this.http
      .put<RestCacheInfo>(`${this.resourceUrl}/${this.getCacheInfoIdentifier(cacheInfo)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(cacheInfo: PartialUpdateCacheInfo): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cacheInfo);
    return this.http
      .patch<RestCacheInfo>(`${this.resourceUrl}/${this.getCacheInfoIdentifier(cacheInfo)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestCacheInfo>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestCacheInfo[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCacheInfoIdentifier(cacheInfo: Pick<ICacheInfo, 'id'>): number {
    return cacheInfo.id;
  }

  compareCacheInfo(o1: Pick<ICacheInfo, 'id'> | null, o2: Pick<ICacheInfo, 'id'> | null): boolean {
    return o1 && o2 ? this.getCacheInfoIdentifier(o1) === this.getCacheInfoIdentifier(o2) : o1 === o2;
  }

  addCacheInfoToCollectionIfMissing<Type extends Pick<ICacheInfo, 'id'>>(
    cacheInfoCollection: Type[],
    ...cacheInfosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const cacheInfos: Type[] = cacheInfosToCheck.filter(isPresent);
    if (cacheInfos.length > 0) {
      const cacheInfoCollectionIdentifiers = cacheInfoCollection.map(cacheInfoItem => this.getCacheInfoIdentifier(cacheInfoItem));
      const cacheInfosToAdd = cacheInfos.filter(cacheInfoItem => {
        const cacheInfoIdentifier = this.getCacheInfoIdentifier(cacheInfoItem);
        if (cacheInfoCollectionIdentifiers.includes(cacheInfoIdentifier)) {
          return false;
        }
        cacheInfoCollectionIdentifiers.push(cacheInfoIdentifier);
        return true;
      });
      return [...cacheInfosToAdd, ...cacheInfoCollection];
    }
    return cacheInfoCollection;
  }

  protected convertDateFromClient<T extends ICacheInfo | NewCacheInfo | PartialUpdateCacheInfo>(cacheInfo: T): RestOf<T> {
    return {
      ...cacheInfo,
      createdDate: cacheInfo.createdDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restCacheInfo: RestCacheInfo): ICacheInfo {
    return {
      ...restCacheInfo,
      createdDate: restCacheInfo.createdDate ? dayjs(restCacheInfo.createdDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestCacheInfo>): HttpResponse<ICacheInfo> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestCacheInfo[]>): HttpResponse<ICacheInfo[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
