import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICin } from '../cin.model';
import { CinService } from '../service/cin.service';

const cinResolve = (route: ActivatedRouteSnapshot): Observable<null | ICin> => {
  const id = route.params.id;
  if (id) {
    return inject(CinService)
      .find(id)
      .pipe(
        mergeMap((cin: HttpResponse<ICin>) => {
          if (cin.body) {
            return of(cin.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default cinResolve;
