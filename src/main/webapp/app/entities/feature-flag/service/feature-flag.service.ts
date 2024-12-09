import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFeatureFlag, NewFeatureFlag } from '../feature-flag.model';

export type PartialUpdateFeatureFlag = Partial<IFeatureFlag> & Pick<IFeatureFlag, 'id'>;

type RestOf<T extends IFeatureFlag | NewFeatureFlag> = Omit<T, 'createdDate' | 'updatedDate'> & {
  createdDate?: string | null;
  updatedDate?: string | null;
};

export type RestFeatureFlag = RestOf<IFeatureFlag>;

export type NewRestFeatureFlag = RestOf<NewFeatureFlag>;

export type PartialUpdateRestFeatureFlag = RestOf<PartialUpdateFeatureFlag>;

export type EntityResponseType = HttpResponse<IFeatureFlag>;
export type EntityArrayResponseType = HttpResponse<IFeatureFlag[]>;

@Injectable({ providedIn: 'root' })
export class FeatureFlagService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/feature-flags');

  create(featureFlag: NewFeatureFlag): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(featureFlag);
    return this.http
      .post<RestFeatureFlag>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(featureFlag: IFeatureFlag): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(featureFlag);
    return this.http
      .put<RestFeatureFlag>(`${this.resourceUrl}/${this.getFeatureFlagIdentifier(featureFlag)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(featureFlag: PartialUpdateFeatureFlag): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(featureFlag);
    return this.http
      .patch<RestFeatureFlag>(`${this.resourceUrl}/${this.getFeatureFlagIdentifier(featureFlag)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestFeatureFlag>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestFeatureFlag[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getFeatureFlagIdentifier(featureFlag: Pick<IFeatureFlag, 'id'>): number {
    return featureFlag.id;
  }

  compareFeatureFlag(o1: Pick<IFeatureFlag, 'id'> | null, o2: Pick<IFeatureFlag, 'id'> | null): boolean {
    return o1 && o2 ? this.getFeatureFlagIdentifier(o1) === this.getFeatureFlagIdentifier(o2) : o1 === o2;
  }

  addFeatureFlagToCollectionIfMissing<Type extends Pick<IFeatureFlag, 'id'>>(
    featureFlagCollection: Type[],
    ...featureFlagsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const featureFlags: Type[] = featureFlagsToCheck.filter(isPresent);
    if (featureFlags.length > 0) {
      const featureFlagCollectionIdentifiers = featureFlagCollection.map(featureFlagItem => this.getFeatureFlagIdentifier(featureFlagItem));
      const featureFlagsToAdd = featureFlags.filter(featureFlagItem => {
        const featureFlagIdentifier = this.getFeatureFlagIdentifier(featureFlagItem);
        if (featureFlagCollectionIdentifiers.includes(featureFlagIdentifier)) {
          return false;
        }
        featureFlagCollectionIdentifiers.push(featureFlagIdentifier);
        return true;
      });
      return [...featureFlagsToAdd, ...featureFlagCollection];
    }
    return featureFlagCollection;
  }

  protected convertDateFromClient<T extends IFeatureFlag | NewFeatureFlag | PartialUpdateFeatureFlag>(featureFlag: T): RestOf<T> {
    return {
      ...featureFlag,
      createdDate: featureFlag.createdDate?.toJSON() ?? null,
      updatedDate: featureFlag.updatedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restFeatureFlag: RestFeatureFlag): IFeatureFlag {
    return {
      ...restFeatureFlag,
      createdDate: restFeatureFlag.createdDate ? dayjs(restFeatureFlag.createdDate) : undefined,
      updatedDate: restFeatureFlag.updatedDate ? dayjs(restFeatureFlag.updatedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestFeatureFlag>): HttpResponse<IFeatureFlag> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestFeatureFlag[]>): HttpResponse<IFeatureFlag[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
