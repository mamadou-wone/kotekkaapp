import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOneTimePassword, NewOneTimePassword } from '../one-time-password.model';

export type PartialUpdateOneTimePassword = Partial<IOneTimePassword> & Pick<IOneTimePassword, 'id'>;

type RestOf<T extends IOneTimePassword | NewOneTimePassword> = Omit<T, 'expiry' | 'createdDate'> & {
  expiry?: string | null;
  createdDate?: string | null;
};

export type RestOneTimePassword = RestOf<IOneTimePassword>;

export type NewRestOneTimePassword = RestOf<NewOneTimePassword>;

export type PartialUpdateRestOneTimePassword = RestOf<PartialUpdateOneTimePassword>;

export type EntityResponseType = HttpResponse<IOneTimePassword>;
export type EntityArrayResponseType = HttpResponse<IOneTimePassword[]>;

@Injectable({ providedIn: 'root' })
export class OneTimePasswordService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/one-time-passwords');

  create(oneTimePassword: NewOneTimePassword): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(oneTimePassword);
    return this.http
      .post<RestOneTimePassword>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(oneTimePassword: IOneTimePassword): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(oneTimePassword);
    return this.http
      .put<RestOneTimePassword>(`${this.resourceUrl}/${this.getOneTimePasswordIdentifier(oneTimePassword)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(oneTimePassword: PartialUpdateOneTimePassword): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(oneTimePassword);
    return this.http
      .patch<RestOneTimePassword>(`${this.resourceUrl}/${this.getOneTimePasswordIdentifier(oneTimePassword)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestOneTimePassword>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestOneTimePassword[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getOneTimePasswordIdentifier(oneTimePassword: Pick<IOneTimePassword, 'id'>): number {
    return oneTimePassword.id;
  }

  compareOneTimePassword(o1: Pick<IOneTimePassword, 'id'> | null, o2: Pick<IOneTimePassword, 'id'> | null): boolean {
    return o1 && o2 ? this.getOneTimePasswordIdentifier(o1) === this.getOneTimePasswordIdentifier(o2) : o1 === o2;
  }

  addOneTimePasswordToCollectionIfMissing<Type extends Pick<IOneTimePassword, 'id'>>(
    oneTimePasswordCollection: Type[],
    ...oneTimePasswordsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const oneTimePasswords: Type[] = oneTimePasswordsToCheck.filter(isPresent);
    if (oneTimePasswords.length > 0) {
      const oneTimePasswordCollectionIdentifiers = oneTimePasswordCollection.map(oneTimePasswordItem =>
        this.getOneTimePasswordIdentifier(oneTimePasswordItem),
      );
      const oneTimePasswordsToAdd = oneTimePasswords.filter(oneTimePasswordItem => {
        const oneTimePasswordIdentifier = this.getOneTimePasswordIdentifier(oneTimePasswordItem);
        if (oneTimePasswordCollectionIdentifiers.includes(oneTimePasswordIdentifier)) {
          return false;
        }
        oneTimePasswordCollectionIdentifiers.push(oneTimePasswordIdentifier);
        return true;
      });
      return [...oneTimePasswordsToAdd, ...oneTimePasswordCollection];
    }
    return oneTimePasswordCollection;
  }

  protected convertDateFromClient<T extends IOneTimePassword | NewOneTimePassword | PartialUpdateOneTimePassword>(
    oneTimePassword: T,
  ): RestOf<T> {
    return {
      ...oneTimePassword,
      expiry: oneTimePassword.expiry?.toJSON() ?? null,
      createdDate: oneTimePassword.createdDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restOneTimePassword: RestOneTimePassword): IOneTimePassword {
    return {
      ...restOneTimePassword,
      expiry: restOneTimePassword.expiry ? dayjs(restOneTimePassword.expiry) : undefined,
      createdDate: restOneTimePassword.createdDate ? dayjs(restOneTimePassword.createdDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestOneTimePassword>): HttpResponse<IOneTimePassword> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestOneTimePassword[]>): HttpResponse<IOneTimePassword[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
