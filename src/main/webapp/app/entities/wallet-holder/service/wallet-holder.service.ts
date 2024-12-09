import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IWalletHolder, NewWalletHolder } from '../wallet-holder.model';

export type PartialUpdateWalletHolder = Partial<IWalletHolder> & Pick<IWalletHolder, 'id'>;

type RestOf<T extends IWalletHolder | NewWalletHolder> = Omit<T, 'dateOfBirth' | 'createdDate' | 'lastModifiedDate'> & {
  dateOfBirth?: string | null;
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

export type RestWalletHolder = RestOf<IWalletHolder>;

export type NewRestWalletHolder = RestOf<NewWalletHolder>;

export type PartialUpdateRestWalletHolder = RestOf<PartialUpdateWalletHolder>;

export type EntityResponseType = HttpResponse<IWalletHolder>;
export type EntityArrayResponseType = HttpResponse<IWalletHolder[]>;

@Injectable({ providedIn: 'root' })
export class WalletHolderService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/wallet-holders');

  create(walletHolder: NewWalletHolder): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(walletHolder);
    return this.http
      .post<RestWalletHolder>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(walletHolder: IWalletHolder): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(walletHolder);
    return this.http
      .put<RestWalletHolder>(`${this.resourceUrl}/${this.getWalletHolderIdentifier(walletHolder)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(walletHolder: PartialUpdateWalletHolder): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(walletHolder);
    return this.http
      .patch<RestWalletHolder>(`${this.resourceUrl}/${this.getWalletHolderIdentifier(walletHolder)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestWalletHolder>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestWalletHolder[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getWalletHolderIdentifier(walletHolder: Pick<IWalletHolder, 'id'>): number {
    return walletHolder.id;
  }

  compareWalletHolder(o1: Pick<IWalletHolder, 'id'> | null, o2: Pick<IWalletHolder, 'id'> | null): boolean {
    return o1 && o2 ? this.getWalletHolderIdentifier(o1) === this.getWalletHolderIdentifier(o2) : o1 === o2;
  }

  addWalletHolderToCollectionIfMissing<Type extends Pick<IWalletHolder, 'id'>>(
    walletHolderCollection: Type[],
    ...walletHoldersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const walletHolders: Type[] = walletHoldersToCheck.filter(isPresent);
    if (walletHolders.length > 0) {
      const walletHolderCollectionIdentifiers = walletHolderCollection.map(walletHolderItem =>
        this.getWalletHolderIdentifier(walletHolderItem),
      );
      const walletHoldersToAdd = walletHolders.filter(walletHolderItem => {
        const walletHolderIdentifier = this.getWalletHolderIdentifier(walletHolderItem);
        if (walletHolderCollectionIdentifiers.includes(walletHolderIdentifier)) {
          return false;
        }
        walletHolderCollectionIdentifiers.push(walletHolderIdentifier);
        return true;
      });
      return [...walletHoldersToAdd, ...walletHolderCollection];
    }
    return walletHolderCollection;
  }

  protected convertDateFromClient<T extends IWalletHolder | NewWalletHolder | PartialUpdateWalletHolder>(walletHolder: T): RestOf<T> {
    return {
      ...walletHolder,
      dateOfBirth: walletHolder.dateOfBirth?.format(DATE_FORMAT) ?? null,
      createdDate: walletHolder.createdDate?.toJSON() ?? null,
      lastModifiedDate: walletHolder.lastModifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restWalletHolder: RestWalletHolder): IWalletHolder {
    return {
      ...restWalletHolder,
      dateOfBirth: restWalletHolder.dateOfBirth ? dayjs(restWalletHolder.dateOfBirth) : undefined,
      createdDate: restWalletHolder.createdDate ? dayjs(restWalletHolder.createdDate) : undefined,
      lastModifiedDate: restWalletHolder.lastModifiedDate ? dayjs(restWalletHolder.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestWalletHolder>): HttpResponse<IWalletHolder> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestWalletHolder[]>): HttpResponse<IWalletHolder[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
