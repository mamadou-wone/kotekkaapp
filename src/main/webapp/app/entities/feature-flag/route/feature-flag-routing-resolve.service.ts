import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFeatureFlag } from '../feature-flag.model';
import { FeatureFlagService } from '../service/feature-flag.service';

const featureFlagResolve = (route: ActivatedRouteSnapshot): Observable<null | IFeatureFlag> => {
  const id = route.params.id;
  if (id) {
    return inject(FeatureFlagService)
      .find(id)
      .pipe(
        mergeMap((featureFlag: HttpResponse<IFeatureFlag>) => {
          if (featureFlag.body) {
            return of(featureFlag.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default featureFlagResolve;
