import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import FailedAttemptResolve from './route/failed-attempt-routing-resolve.service';

const failedAttemptRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/failed-attempt.component').then(m => m.FailedAttemptComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/failed-attempt-detail.component').then(m => m.FailedAttemptDetailComponent),
    resolve: {
      failedAttempt: FailedAttemptResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/failed-attempt-update.component').then(m => m.FailedAttemptUpdateComponent),
    resolve: {
      failedAttempt: FailedAttemptResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/failed-attempt-update.component').then(m => m.FailedAttemptUpdateComponent),
    resolve: {
      failedAttempt: FailedAttemptResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default failedAttemptRoute;
