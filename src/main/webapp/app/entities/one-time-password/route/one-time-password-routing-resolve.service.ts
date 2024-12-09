import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOneTimePassword } from '../one-time-password.model';
import { OneTimePasswordService } from '../service/one-time-password.service';

const oneTimePasswordResolve = (route: ActivatedRouteSnapshot): Observable<null | IOneTimePassword> => {
  const id = route.params.id;
  if (id) {
    return inject(OneTimePasswordService)
      .find(id)
      .pipe(
        mergeMap((oneTimePassword: HttpResponse<IOneTimePassword>) => {
          if (oneTimePassword.body) {
            return of(oneTimePassword.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default oneTimePasswordResolve;
