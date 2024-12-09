import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IReferalInfo, NewReferalInfo } from '../referal-info.model';

export type PartialUpdateReferalInfo = Partial<IReferalInfo> & Pick<IReferalInfo, 'id'>;

type RestOf<T extends IReferalInfo | NewReferalInfo> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

export type RestReferalInfo = RestOf<IReferalInfo>;

export type NewRestReferalInfo = RestOf<NewReferalInfo>;

export type PartialUpdateRestReferalInfo = RestOf<PartialUpdateReferalInfo>;

export type EntityResponseType = HttpResponse<IReferalInfo>;
export type EntityArrayResponseType = HttpResponse<IReferalInfo[]>;

@Injectable({ providedIn: 'root' })
export class ReferalInfoService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/referal-infos');

  create(referalInfo: NewReferalInfo): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(referalInfo);
    return this.http
      .post<RestReferalInfo>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(referalInfo: IReferalInfo): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(referalInfo);
    return this.http
      .put<RestReferalInfo>(`${this.resourceUrl}/${this.getReferalInfoIdentifier(referalInfo)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(referalInfo: PartialUpdateReferalInfo): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(referalInfo);
    return this.http
      .patch<RestReferalInfo>(`${this.resourceUrl}/${this.getReferalInfoIdentifier(referalInfo)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestReferalInfo>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestReferalInfo[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getReferalInfoIdentifier(referalInfo: Pick<IReferalInfo, 'id'>): number {
    return referalInfo.id;
  }

  compareReferalInfo(o1: Pick<IReferalInfo, 'id'> | null, o2: Pick<IReferalInfo, 'id'> | null): boolean {
    return o1 && o2 ? this.getReferalInfoIdentifier(o1) === this.getReferalInfoIdentifier(o2) : o1 === o2;
  }

  addReferalInfoToCollectionIfMissing<Type extends Pick<IReferalInfo, 'id'>>(
    referalInfoCollection: Type[],
    ...referalInfosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const referalInfos: Type[] = referalInfosToCheck.filter(isPresent);
    if (referalInfos.length > 0) {
      const referalInfoCollectionIdentifiers = referalInfoCollection.map(referalInfoItem => this.getReferalInfoIdentifier(referalInfoItem));
      const referalInfosToAdd = referalInfos.filter(referalInfoItem => {
        const referalInfoIdentifier = this.getReferalInfoIdentifier(referalInfoItem);
        if (referalInfoCollectionIdentifiers.includes(referalInfoIdentifier)) {
          return false;
        }
        referalInfoCollectionIdentifiers.push(referalInfoIdentifier);
        return true;
      });
      return [...referalInfosToAdd, ...referalInfoCollection];
    }
    return referalInfoCollection;
  }

  protected convertDateFromClient<T extends IReferalInfo | NewReferalInfo | PartialUpdateReferalInfo>(referalInfo: T): RestOf<T> {
    return {
      ...referalInfo,
      createdDate: referalInfo.createdDate?.toJSON() ?? null,
      lastModifiedDate: referalInfo.lastModifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restReferalInfo: RestReferalInfo): IReferalInfo {
    return {
      ...restReferalInfo,
      createdDate: restReferalInfo.createdDate ? dayjs(restReferalInfo.createdDate) : undefined,
      lastModifiedDate: restReferalInfo.lastModifiedDate ? dayjs(restReferalInfo.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestReferalInfo>): HttpResponse<IReferalInfo> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestReferalInfo[]>): HttpResponse<IReferalInfo[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
