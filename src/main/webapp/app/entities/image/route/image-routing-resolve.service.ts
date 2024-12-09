import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IImage } from '../image.model';
import { ImageService } from '../service/image.service';

const imageResolve = (route: ActivatedRouteSnapshot): Observable<null | IImage> => {
  const id = route.params.id;
  if (id) {
    return inject(ImageService)
      .find(id)
      .pipe(
        mergeMap((image: HttpResponse<IImage>) => {
          if (image.body) {
            return of(image.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default imageResolve;
