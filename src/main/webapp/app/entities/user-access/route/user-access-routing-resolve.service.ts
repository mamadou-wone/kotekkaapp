import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IUserAccess } from '../user-access.model';
import { UserAccessService } from '../service/user-access.service';

const userAccessResolve = (route: ActivatedRouteSnapshot): Observable<null | IUserAccess> => {
  const id = route.params.id;
  if (id) {
    return inject(UserAccessService)
      .find(id)
      .pipe(
        mergeMap((userAccess: HttpResponse<IUserAccess>) => {
          if (userAccess.body) {
            return of(userAccess.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default userAccessResolve;
