import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IWalletHolder } from '../wallet-holder.model';
import { WalletHolderService } from '../service/wallet-holder.service';

const walletHolderResolve = (route: ActivatedRouteSnapshot): Observable<null | IWalletHolder> => {
  const id = route.params.id;
  if (id) {
    return inject(WalletHolderService)
      .find(id)
      .pipe(
        mergeMap((walletHolder: HttpResponse<IWalletHolder>) => {
          if (walletHolder.body) {
            return of(walletHolder.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default walletHolderResolve;
