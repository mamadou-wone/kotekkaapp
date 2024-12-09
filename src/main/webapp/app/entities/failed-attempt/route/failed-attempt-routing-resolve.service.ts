import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFailedAttempt } from '../failed-attempt.model';
import { FailedAttemptService } from '../service/failed-attempt.service';

const failedAttemptResolve = (route: ActivatedRouteSnapshot): Observable<null | IFailedAttempt> => {
  const id = route.params.id;
  if (id) {
    return inject(FailedAttemptService)
      .find(id)
      .pipe(
        mergeMap((failedAttempt: HttpResponse<IFailedAttempt>) => {
          if (failedAttempt.body) {
            return of(failedAttempt.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default failedAttemptResolve;
