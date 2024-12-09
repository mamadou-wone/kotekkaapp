import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUserAffiliation, NewUserAffiliation } from '../user-affiliation.model';

export type PartialUpdateUserAffiliation = Partial<IUserAffiliation> & Pick<IUserAffiliation, 'id'>;

type RestOf<T extends IUserAffiliation | NewUserAffiliation> = Omit<T, 'createdDate'> & {
  createdDate?: string | null;
};

export type RestUserAffiliation = RestOf<IUserAffiliation>;

export type NewRestUserAffiliation = RestOf<NewUserAffiliation>;

export type PartialUpdateRestUserAffiliation = RestOf<PartialUpdateUserAffiliation>;

export type EntityResponseType = HttpResponse<IUserAffiliation>;
export type EntityArrayResponseType = HttpResponse<IUserAffiliation[]>;

@Injectable({ providedIn: 'root' })
export class UserAffiliationService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/user-affiliations');

  create(userAffiliation: NewUserAffiliation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(userAffiliation);
    return this.http
      .post<RestUserAffiliation>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(userAffiliation: IUserAffiliation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(userAffiliation);
    return this.http
      .put<RestUserAffiliation>(`${this.resourceUrl}/${this.getUserAffiliationIdentifier(userAffiliation)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(userAffiliation: PartialUpdateUserAffiliation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(userAffiliation);
    return this.http
      .patch<RestUserAffiliation>(`${this.resourceUrl}/${this.getUserAffiliationIdentifier(userAffiliation)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestUserAffiliation>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestUserAffiliation[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getUserAffiliationIdentifier(userAffiliation: Pick<IUserAffiliation, 'id'>): number {
    return userAffiliation.id;
  }

  compareUserAffiliation(o1: Pick<IUserAffiliation, 'id'> | null, o2: Pick<IUserAffiliation, 'id'> | null): boolean {
    return o1 && o2 ? this.getUserAffiliationIdentifier(o1) === this.getUserAffiliationIdentifier(o2) : o1 === o2;
  }

  addUserAffiliationToCollectionIfMissing<Type extends Pick<IUserAffiliation, 'id'>>(
    userAffiliationCollection: Type[],
    ...userAffiliationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const userAffiliations: Type[] = userAffiliationsToCheck.filter(isPresent);
    if (userAffiliations.length > 0) {
      const userAffiliationCollectionIdentifiers = userAffiliationCollection.map(userAffiliationItem =>
        this.getUserAffiliationIdentifier(userAffiliationItem),
      );
      const userAffiliationsToAdd = userAffiliations.filter(userAffiliationItem => {
        const userAffiliationIdentifier = this.getUserAffiliationIdentifier(userAffiliationItem);
        if (userAffiliationCollectionIdentifiers.includes(userAffiliationIdentifier)) {
          return false;
        }
        userAffiliationCollectionIdentifiers.push(userAffiliationIdentifier);
        return true;
      });
      return [...userAffiliationsToAdd, ...userAffiliationCollection];
    }
    return userAffiliationCollection;
  }

  protected convertDateFromClient<T extends IUserAffiliation | NewUserAffiliation | PartialUpdateUserAffiliation>(
    userAffiliation: T,
  ): RestOf<T> {
    return {
      ...userAffiliation,
      createdDate: userAffiliation.createdDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restUserAffiliation: RestUserAffiliation): IUserAffiliation {
    return {
      ...restUserAffiliation,
      createdDate: restUserAffiliation.createdDate ? dayjs(restUserAffiliation.createdDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestUserAffiliation>): HttpResponse<IUserAffiliation> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestUserAffiliation[]>): HttpResponse<IUserAffiliation[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
