import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRecipient, NewRecipient } from '../recipient.model';

export type PartialUpdateRecipient = Partial<IRecipient> & Pick<IRecipient, 'id'>;

type RestOf<T extends IRecipient | NewRecipient> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

export type RestRecipient = RestOf<IRecipient>;

export type NewRestRecipient = RestOf<NewRecipient>;

export type PartialUpdateRestRecipient = RestOf<PartialUpdateRecipient>;

export type EntityResponseType = HttpResponse<IRecipient>;
export type EntityArrayResponseType = HttpResponse<IRecipient[]>;

@Injectable({ providedIn: 'root' })
export class RecipientService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/recipients');

  create(recipient: NewRecipient): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(recipient);
    return this.http
      .post<RestRecipient>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(recipient: IRecipient): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(recipient);
    return this.http
      .put<RestRecipient>(`${this.resourceUrl}/${this.getRecipientIdentifier(recipient)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(recipient: PartialUpdateRecipient): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(recipient);
    return this.http
      .patch<RestRecipient>(`${this.resourceUrl}/${this.getRecipientIdentifier(recipient)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestRecipient>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestRecipient[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getRecipientIdentifier(recipient: Pick<IRecipient, 'id'>): number {
    return recipient.id;
  }

  compareRecipient(o1: Pick<IRecipient, 'id'> | null, o2: Pick<IRecipient, 'id'> | null): boolean {
    return o1 && o2 ? this.getRecipientIdentifier(o1) === this.getRecipientIdentifier(o2) : o1 === o2;
  }

  addRecipientToCollectionIfMissing<Type extends Pick<IRecipient, 'id'>>(
    recipientCollection: Type[],
    ...recipientsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const recipients: Type[] = recipientsToCheck.filter(isPresent);
    if (recipients.length > 0) {
      const recipientCollectionIdentifiers = recipientCollection.map(recipientItem => this.getRecipientIdentifier(recipientItem));
      const recipientsToAdd = recipients.filter(recipientItem => {
        const recipientIdentifier = this.getRecipientIdentifier(recipientItem);
        if (recipientCollectionIdentifiers.includes(recipientIdentifier)) {
          return false;
        }
        recipientCollectionIdentifiers.push(recipientIdentifier);
        return true;
      });
      return [...recipientsToAdd, ...recipientCollection];
    }
    return recipientCollection;
  }

  protected convertDateFromClient<T extends IRecipient | NewRecipient | PartialUpdateRecipient>(recipient: T): RestOf<T> {
    return {
      ...recipient,
      createdDate: recipient.createdDate?.toJSON() ?? null,
      lastModifiedDate: recipient.lastModifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restRecipient: RestRecipient): IRecipient {
    return {
      ...restRecipient,
      createdDate: restRecipient.createdDate ? dayjs(restRecipient.createdDate) : undefined,
      lastModifiedDate: restRecipient.lastModifiedDate ? dayjs(restRecipient.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestRecipient>): HttpResponse<IRecipient> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestRecipient[]>): HttpResponse<IRecipient[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
