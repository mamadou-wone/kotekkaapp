import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import OneTimePasswordResolve from './route/one-time-password-routing-resolve.service';

const oneTimePasswordRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/one-time-password.component').then(m => m.OneTimePasswordComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/one-time-password-detail.component').then(m => m.OneTimePasswordDetailComponent),
    resolve: {
      oneTimePassword: OneTimePasswordResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/one-time-password-update.component').then(m => m.OneTimePasswordUpdateComponent),
    resolve: {
      oneTimePassword: OneTimePasswordResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/one-time-password-update.component').then(m => m.OneTimePasswordUpdateComponent),
    resolve: {
      oneTimePassword: OneTimePasswordResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default oneTimePasswordRoute;
