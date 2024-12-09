import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICin, NewCin } from '../cin.model';

export type PartialUpdateCin = Partial<ICin> & Pick<ICin, 'id'>;

type RestOf<T extends ICin | NewCin> = Omit<T, 'validityDate' | 'birthDate' | 'createdDate'> & {
  validityDate?: string | null;
  birthDate?: string | null;
  createdDate?: string | null;
};

export type RestCin = RestOf<ICin>;

export type NewRestCin = RestOf<NewCin>;

export type PartialUpdateRestCin = RestOf<PartialUpdateCin>;

export type EntityResponseType = HttpResponse<ICin>;
export type EntityArrayResponseType = HttpResponse<ICin[]>;

@Injectable({ providedIn: 'root' })
export class CinService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/cins');

  create(cin: NewCin): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cin);
    return this.http.post<RestCin>(this.resourceUrl, copy, { observe: 'response' }).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(cin: ICin): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cin);
    return this.http
      .put<RestCin>(`${this.resourceUrl}/${this.getCinIdentifier(cin)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(cin: PartialUpdateCin): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cin);
    return this.http
      .patch<RestCin>(`${this.resourceUrl}/${this.getCinIdentifier(cin)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestCin>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestCin[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCinIdentifier(cin: Pick<ICin, 'id'>): number {
    return cin.id;
  }

  compareCin(o1: Pick<ICin, 'id'> | null, o2: Pick<ICin, 'id'> | null): boolean {
    return o1 && o2 ? this.getCinIdentifier(o1) === this.getCinIdentifier(o2) : o1 === o2;
  }

  addCinToCollectionIfMissing<Type extends Pick<ICin, 'id'>>(cinCollection: Type[], ...cinsToCheck: (Type | null | undefined)[]): Type[] {
    const cins: Type[] = cinsToCheck.filter(isPresent);
    if (cins.length > 0) {
      const cinCollectionIdentifiers = cinCollection.map(cinItem => this.getCinIdentifier(cinItem));
      const cinsToAdd = cins.filter(cinItem => {
        const cinIdentifier = this.getCinIdentifier(cinItem);
        if (cinCollectionIdentifiers.includes(cinIdentifier)) {
          return false;
        }
        cinCollectionIdentifiers.push(cinIdentifier);
        return true;
      });
      return [...cinsToAdd, ...cinCollection];
    }
    return cinCollection;
  }

  protected convertDateFromClient<T extends ICin | NewCin | PartialUpdateCin>(cin: T): RestOf<T> {
    return {
      ...cin,
      validityDate: cin.validityDate?.format(DATE_FORMAT) ?? null,
      birthDate: cin.birthDate?.format(DATE_FORMAT) ?? null,
      createdDate: cin.createdDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restCin: RestCin): ICin {
    return {
      ...restCin,
      validityDate: restCin.validityDate ? dayjs(restCin.validityDate) : undefined,
      birthDate: restCin.birthDate ? dayjs(restCin.birthDate) : undefined,
      createdDate: restCin.createdDate ? dayjs(restCin.createdDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestCin>): HttpResponse<ICin> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestCin[]>): HttpResponse<ICin[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
