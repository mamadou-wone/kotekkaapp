import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import FailedAttemptHistoryResolve from './route/failed-attempt-history-routing-resolve.service';

const failedAttemptHistoryRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/failed-attempt-history.component').then(m => m.FailedAttemptHistoryComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/failed-attempt-history-detail.component').then(m => m.FailedAttemptHistoryDetailComponent),
    resolve: {
      failedAttemptHistory: FailedAttemptHistoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/failed-attempt-history-update.component').then(m => m.FailedAttemptHistoryUpdateComponent),
    resolve: {
      failedAttemptHistory: FailedAttemptHistoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/failed-attempt-history-update.component').then(m => m.FailedAttemptHistoryUpdateComponent),
    resolve: {
      failedAttemptHistory: FailedAttemptHistoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default failedAttemptHistoryRoute;
