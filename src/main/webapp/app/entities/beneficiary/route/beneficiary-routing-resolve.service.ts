import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBeneficiary } from '../beneficiary.model';
import { BeneficiaryService } from '../service/beneficiary.service';

const beneficiaryResolve = (route: ActivatedRouteSnapshot): Observable<null | IBeneficiary> => {
  const id = route.params.id;
  if (id) {
    return inject(BeneficiaryService)
      .find(id)
      .pipe(
        mergeMap((beneficiary: HttpResponse<IBeneficiary>) => {
          if (beneficiary.body) {
            return of(beneficiary.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default beneficiaryResolve;
