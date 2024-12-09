import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IUserAffiliation } from '../user-affiliation.model';
import { UserAffiliationService } from '../service/user-affiliation.service';

const userAffiliationResolve = (route: ActivatedRouteSnapshot): Observable<null | IUserAffiliation> => {
  const id = route.params.id;
  if (id) {
    return inject(UserAffiliationService)
      .find(id)
      .pipe(
        mergeMap((userAffiliation: HttpResponse<IUserAffiliation>) => {
          if (userAffiliation.body) {
            return of(userAffiliation.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default userAffiliationResolve;
