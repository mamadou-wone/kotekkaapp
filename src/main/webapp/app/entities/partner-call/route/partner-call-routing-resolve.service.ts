import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPartnerCall } from '../partner-call.model';
import { PartnerCallService } from '../service/partner-call.service';

const partnerCallResolve = (route: ActivatedRouteSnapshot): Observable<null | IPartnerCall> => {
  const id = route.params.id;
  if (id) {
    return inject(PartnerCallService)
      .find(id)
      .pipe(
        mergeMap((partnerCall: HttpResponse<IPartnerCall>) => {
          if (partnerCall.body) {
            return of(partnerCall.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default partnerCallResolve;
