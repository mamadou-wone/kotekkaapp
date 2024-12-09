import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IServiceClient, NewServiceClient } from '../service-client.model';

export type PartialUpdateServiceClient = Partial<IServiceClient> & Pick<IServiceClient, 'id'>;

type RestOf<T extends IServiceClient | NewServiceClient> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

export type RestServiceClient = RestOf<IServiceClient>;

export type NewRestServiceClient = RestOf<NewServiceClient>;

export type PartialUpdateRestServiceClient = RestOf<PartialUpdateServiceClient>;

export type EntityResponseType = HttpResponse<IServiceClient>;
export type EntityArrayResponseType = HttpResponse<IServiceClient[]>;

@Injectable({ providedIn: 'root' })
export class ServiceClientService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/service-clients');

  create(serviceClient: NewServiceClient): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(serviceClient);
    return this.http
      .post<RestServiceClient>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(serviceClient: IServiceClient): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(serviceClient);
    return this.http
      .put<RestServiceClient>(`${this.resourceUrl}/${this.getServiceClientIdentifier(serviceClient)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(serviceClient: PartialUpdateServiceClient): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(serviceClient);
    return this.http
      .patch<RestServiceClient>(`${this.resourceUrl}/${this.getServiceClientIdentifier(serviceClient)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestServiceClient>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestServiceClient[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getServiceClientIdentifier(serviceClient: Pick<IServiceClient, 'id'>): number {
    return serviceClient.id;
  }

  compareServiceClient(o1: Pick<IServiceClient, 'id'> | null, o2: Pick<IServiceClient, 'id'> | null): boolean {
    return o1 && o2 ? this.getServiceClientIdentifier(o1) === this.getServiceClientIdentifier(o2) : o1 === o2;
  }

  addServiceClientToCollectionIfMissing<Type extends Pick<IServiceClient, 'id'>>(
    serviceClientCollection: Type[],
    ...serviceClientsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const serviceClients: Type[] = serviceClientsToCheck.filter(isPresent);
    if (serviceClients.length > 0) {
      const serviceClientCollectionIdentifiers = serviceClientCollection.map(serviceClientItem =>
        this.getServiceClientIdentifier(serviceClientItem),
      );
      const serviceClientsToAdd = serviceClients.filter(serviceClientItem => {
        const serviceClientIdentifier = this.getServiceClientIdentifier(serviceClientItem);
        if (serviceClientCollectionIdentifiers.includes(serviceClientIdentifier)) {
          return false;
        }
        serviceClientCollectionIdentifiers.push(serviceClientIdentifier);
        return true;
      });
      return [...serviceClientsToAdd, ...serviceClientCollection];
    }
    return serviceClientCollection;
  }

  protected convertDateFromClient<T extends IServiceClient | NewServiceClient | PartialUpdateServiceClient>(serviceClient: T): RestOf<T> {
    return {
      ...serviceClient,
      createdDate: serviceClient.createdDate?.toJSON() ?? null,
      lastModifiedDate: serviceClient.lastModifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restServiceClient: RestServiceClient): IServiceClient {
    return {
      ...restServiceClient,
      createdDate: restServiceClient.createdDate ? dayjs(restServiceClient.createdDate) : undefined,
      lastModifiedDate: restServiceClient.lastModifiedDate ? dayjs(restServiceClient.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestServiceClient>): HttpResponse<IServiceClient> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestServiceClient[]>): HttpResponse<IServiceClient[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
