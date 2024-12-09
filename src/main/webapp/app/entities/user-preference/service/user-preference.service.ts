import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUserPreference, NewUserPreference } from '../user-preference.model';

export type PartialUpdateUserPreference = Partial<IUserPreference> & Pick<IUserPreference, 'id'>;

type RestOf<T extends IUserPreference | NewUserPreference> = Omit<T, 'createdDate'> & {
  createdDate?: string | null;
};

export type RestUserPreference = RestOf<IUserPreference>;

export type NewRestUserPreference = RestOf<NewUserPreference>;

export type PartialUpdateRestUserPreference = RestOf<PartialUpdateUserPreference>;

export type EntityResponseType = HttpResponse<IUserPreference>;
export type EntityArrayResponseType = HttpResponse<IUserPreference[]>;

@Injectable({ providedIn: 'root' })
export class UserPreferenceService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/user-preferences');

  create(userPreference: NewUserPreference): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(userPreference);
    return this.http
      .post<RestUserPreference>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(userPreference: IUserPreference): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(userPreference);
    return this.http
      .put<RestUserPreference>(`${this.resourceUrl}/${this.getUserPreferenceIdentifier(userPreference)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(userPreference: PartialUpdateUserPreference): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(userPreference);
    return this.http
      .patch<RestUserPreference>(`${this.resourceUrl}/${this.getUserPreferenceIdentifier(userPreference)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestUserPreference>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestUserPreference[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getUserPreferenceIdentifier(userPreference: Pick<IUserPreference, 'id'>): number {
    return userPreference.id;
  }

  compareUserPreference(o1: Pick<IUserPreference, 'id'> | null, o2: Pick<IUserPreference, 'id'> | null): boolean {
    return o1 && o2 ? this.getUserPreferenceIdentifier(o1) === this.getUserPreferenceIdentifier(o2) : o1 === o2;
  }

  addUserPreferenceToCollectionIfMissing<Type extends Pick<IUserPreference, 'id'>>(
    userPreferenceCollection: Type[],
    ...userPreferencesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const userPreferences: Type[] = userPreferencesToCheck.filter(isPresent);
    if (userPreferences.length > 0) {
      const userPreferenceCollectionIdentifiers = userPreferenceCollection.map(userPreferenceItem =>
        this.getUserPreferenceIdentifier(userPreferenceItem),
      );
      const userPreferencesToAdd = userPreferences.filter(userPreferenceItem => {
        const userPreferenceIdentifier = this.getUserPreferenceIdentifier(userPreferenceItem);
        if (userPreferenceCollectionIdentifiers.includes(userPreferenceIdentifier)) {
          return false;
        }
        userPreferenceCollectionIdentifiers.push(userPreferenceIdentifier);
        return true;
      });
      return [...userPreferencesToAdd, ...userPreferenceCollection];
    }
    return userPreferenceCollection;
  }

  protected convertDateFromClient<T extends IUserPreference | NewUserPreference | PartialUpdateUserPreference>(
    userPreference: T,
  ): RestOf<T> {
    return {
      ...userPreference,
      createdDate: userPreference.createdDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restUserPreference: RestUserPreference): IUserPreference {
    return {
      ...restUserPreference,
      createdDate: restUserPreference.createdDate ? dayjs(restUserPreference.createdDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestUserPreference>): HttpResponse<IUserPreference> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestUserPreference[]>): HttpResponse<IUserPreference[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
