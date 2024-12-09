import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFailedAttemptHistory } from '../failed-attempt-history.model';
import { FailedAttemptHistoryService } from '../service/failed-attempt-history.service';

const failedAttemptHistoryResolve = (route: ActivatedRouteSnapshot): Observable<null | IFailedAttemptHistory> => {
  const id = route.params.id;
  if (id) {
    return inject(FailedAttemptHistoryService)
      .find(id)
      .pipe(
        mergeMap((failedAttemptHistory: HttpResponse<IFailedAttemptHistory>) => {
          if (failedAttemptHistory.body) {
            return of(failedAttemptHistory.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default failedAttemptHistoryResolve;
