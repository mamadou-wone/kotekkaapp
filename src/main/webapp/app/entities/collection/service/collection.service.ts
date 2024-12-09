import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICollection, NewCollection } from '../collection.model';

export type PartialUpdateCollection = Partial<ICollection> & Pick<ICollection, 'id'>;

export type EntityResponseType = HttpResponse<ICollection>;
export type EntityArrayResponseType = HttpResponse<ICollection[]>;

@Injectable({ providedIn: 'root' })
export class CollectionService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/collections');

  create(collection: NewCollection): Observable<EntityResponseType> {
    return this.http.post<ICollection>(this.resourceUrl, collection, { observe: 'response' });
  }

  update(collection: ICollection): Observable<EntityResponseType> {
    return this.http.put<ICollection>(`${this.resourceUrl}/${this.getCollectionIdentifier(collection)}`, collection, {
      observe: 'response',
    });
  }

  partialUpdate(collection: PartialUpdateCollection): Observable<EntityResponseType> {
    return this.http.patch<ICollection>(`${this.resourceUrl}/${this.getCollectionIdentifier(collection)}`, collection, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICollection>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICollection[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCollectionIdentifier(collection: Pick<ICollection, 'id'>): number {
    return collection.id;
  }

  compareCollection(o1: Pick<ICollection, 'id'> | null, o2: Pick<ICollection, 'id'> | null): boolean {
    return o1 && o2 ? this.getCollectionIdentifier(o1) === this.getCollectionIdentifier(o2) : o1 === o2;
  }

  addCollectionToCollectionIfMissing<Type extends Pick<ICollection, 'id'>>(
    collectionCollection: Type[],
    ...collectionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const collections: Type[] = collectionsToCheck.filter(isPresent);
    if (collections.length > 0) {
      const collectionCollectionIdentifiers = collectionCollection.map(collectionItem => this.getCollectionIdentifier(collectionItem));
      const collectionsToAdd = collections.filter(collectionItem => {
        const collectionIdentifier = this.getCollectionIdentifier(collectionItem);
        if (collectionCollectionIdentifiers.includes(collectionIdentifier)) {
          return false;
        }
        collectionCollectionIdentifiers.push(collectionIdentifier);
        return true;
      });
      return [...collectionsToAdd, ...collectionCollection];
    }
    return collectionCollection;
  }
}
