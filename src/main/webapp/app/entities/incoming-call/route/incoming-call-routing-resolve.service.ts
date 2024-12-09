import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IIncomingCall } from '../incoming-call.model';
import { IncomingCallService } from '../service/incoming-call.service';

const incomingCallResolve = (route: ActivatedRouteSnapshot): Observable<null | IIncomingCall> => {
  const id = route.params.id;
  if (id) {
    return inject(IncomingCallService)
      .find(id)
      .pipe(
        mergeMap((incomingCall: HttpResponse<IIncomingCall>) => {
          if (incomingCall.body) {
            return of(incomingCall.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default incomingCallResolve;
