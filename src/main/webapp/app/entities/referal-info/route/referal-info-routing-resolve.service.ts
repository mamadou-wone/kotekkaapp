import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IReferalInfo } from '../referal-info.model';
import { ReferalInfoService } from '../service/referal-info.service';

const referalInfoResolve = (route: ActivatedRouteSnapshot): Observable<null | IReferalInfo> => {
  const id = route.params.id;
  if (id) {
    return inject(ReferalInfoService)
      .find(id)
      .pipe(
        mergeMap((referalInfo: HttpResponse<IReferalInfo>) => {
          if (referalInfo.body) {
            return of(referalInfo.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default referalInfoResolve;
