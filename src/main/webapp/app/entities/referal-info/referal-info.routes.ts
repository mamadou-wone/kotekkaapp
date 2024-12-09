import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ReferalInfoResolve from './route/referal-info-routing-resolve.service';

const referalInfoRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/referal-info.component').then(m => m.ReferalInfoComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/referal-info-detail.component').then(m => m.ReferalInfoDetailComponent),
    resolve: {
      referalInfo: ReferalInfoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/referal-info-update.component').then(m => m.ReferalInfoUpdateComponent),
    resolve: {
      referalInfo: ReferalInfoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/referal-info-update.component').then(m => m.ReferalInfoUpdateComponent),
    resolve: {
      referalInfo: ReferalInfoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default referalInfoRoute;
