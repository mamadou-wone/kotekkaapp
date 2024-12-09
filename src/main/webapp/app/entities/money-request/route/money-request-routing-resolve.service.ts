import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMoneyRequest } from '../money-request.model';
import { MoneyRequestService } from '../service/money-request.service';

const moneyRequestResolve = (route: ActivatedRouteSnapshot): Observable<null | IMoneyRequest> => {
  const id = route.params.id;
  if (id) {
    return inject(MoneyRequestService)
      .find(id)
      .pipe(
        mergeMap((moneyRequest: HttpResponse<IMoneyRequest>) => {
          if (moneyRequest.body) {
            return of(moneyRequest.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default moneyRequestResolve;
