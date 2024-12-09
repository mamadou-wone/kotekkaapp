import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IServiceClient } from '../service-client.model';
import { ServiceClientService } from '../service/service-client.service';

const serviceClientResolve = (route: ActivatedRouteSnapshot): Observable<null | IServiceClient> => {
  const id = route.params.id;
  if (id) {
    return inject(ServiceClientService)
      .find(id)
      .pipe(
        mergeMap((serviceClient: HttpResponse<IServiceClient>) => {
          if (serviceClient.body) {
            return of(serviceClient.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default serviceClientResolve;
