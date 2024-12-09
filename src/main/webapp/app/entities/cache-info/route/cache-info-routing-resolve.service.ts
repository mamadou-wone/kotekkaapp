import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICacheInfo } from '../cache-info.model';
import { CacheInfoService } from '../service/cache-info.service';

const cacheInfoResolve = (route: ActivatedRouteSnapshot): Observable<null | ICacheInfo> => {
  const id = route.params.id;
  if (id) {
    return inject(CacheInfoService)
      .find(id)
      .pipe(
        mergeMap((cacheInfo: HttpResponse<ICacheInfo>) => {
          if (cacheInfo.body) {
            return of(cacheInfo.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default cacheInfoResolve;
