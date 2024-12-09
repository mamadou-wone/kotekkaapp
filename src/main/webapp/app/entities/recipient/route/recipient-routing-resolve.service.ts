import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRecipient } from '../recipient.model';
import { RecipientService } from '../service/recipient.service';

const recipientResolve = (route: ActivatedRouteSnapshot): Observable<null | IRecipient> => {
  const id = route.params.id;
  if (id) {
    return inject(RecipientService)
      .find(id)
      .pipe(
        mergeMap((recipient: HttpResponse<IRecipient>) => {
          if (recipient.body) {
            return of(recipient.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default recipientResolve;
