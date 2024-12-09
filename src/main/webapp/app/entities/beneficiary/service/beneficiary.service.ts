import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBeneficiary, NewBeneficiary } from '../beneficiary.model';

export type PartialUpdateBeneficiary = Partial<IBeneficiary> & Pick<IBeneficiary, 'id'>;

type RestOf<T extends IBeneficiary | NewBeneficiary> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

export type RestBeneficiary = RestOf<IBeneficiary>;

export type NewRestBeneficiary = RestOf<NewBeneficiary>;

export type PartialUpdateRestBeneficiary = RestOf<PartialUpdateBeneficiary>;

export type EntityResponseType = HttpResponse<IBeneficiary>;
export type EntityArrayResponseType = HttpResponse<IBeneficiary[]>;

@Injectable({ providedIn: 'root' })
export class BeneficiaryService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/beneficiaries');

  create(beneficiary: NewBeneficiary): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(beneficiary);
    return this.http
      .post<RestBeneficiary>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(beneficiary: IBeneficiary): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(beneficiary);
    return this.http
      .put<RestBeneficiary>(`${this.resourceUrl}/${this.getBeneficiaryIdentifier(beneficiary)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(beneficiary: PartialUpdateBeneficiary): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(beneficiary);
    return this.http
      .patch<RestBeneficiary>(`${this.resourceUrl}/${this.getBeneficiaryIdentifier(beneficiary)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestBeneficiary>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestBeneficiary[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getBeneficiaryIdentifier(beneficiary: Pick<IBeneficiary, 'id'>): number {
    return beneficiary.id;
  }

  compareBeneficiary(o1: Pick<IBeneficiary, 'id'> | null, o2: Pick<IBeneficiary, 'id'> | null): boolean {
    return o1 && o2 ? this.getBeneficiaryIdentifier(o1) === this.getBeneficiaryIdentifier(o2) : o1 === o2;
  }

  addBeneficiaryToCollectionIfMissing<Type extends Pick<IBeneficiary, 'id'>>(
    beneficiaryCollection: Type[],
    ...beneficiariesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const beneficiaries: Type[] = beneficiariesToCheck.filter(isPresent);
    if (beneficiaries.length > 0) {
      const beneficiaryCollectionIdentifiers = beneficiaryCollection.map(beneficiaryItem => this.getBeneficiaryIdentifier(beneficiaryItem));
      const beneficiariesToAdd = beneficiaries.filter(beneficiaryItem => {
        const beneficiaryIdentifier = this.getBeneficiaryIdentifier(beneficiaryItem);
        if (beneficiaryCollectionIdentifiers.includes(beneficiaryIdentifier)) {
          return false;
        }
        beneficiaryCollectionIdentifiers.push(beneficiaryIdentifier);
        return true;
      });
      return [...beneficiariesToAdd, ...beneficiaryCollection];
    }
    return beneficiaryCollection;
  }

  protected convertDateFromClient<T extends IBeneficiary | NewBeneficiary | PartialUpdateBeneficiary>(beneficiary: T): RestOf<T> {
    return {
      ...beneficiary,
      createdDate: beneficiary.createdDate?.toJSON() ?? null,
      lastModifiedDate: beneficiary.lastModifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restBeneficiary: RestBeneficiary): IBeneficiary {
    return {
      ...restBeneficiary,
      createdDate: restBeneficiary.createdDate ? dayjs(restBeneficiary.createdDate) : undefined,
      lastModifiedDate: restBeneficiary.lastModifiedDate ? dayjs(restBeneficiary.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestBeneficiary>): HttpResponse<IBeneficiary> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestBeneficiary[]>): HttpResponse<IBeneficiary[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
