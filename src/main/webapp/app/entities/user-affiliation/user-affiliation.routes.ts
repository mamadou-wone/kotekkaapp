import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import UserAffiliationResolve from './route/user-affiliation-routing-resolve.service';

const userAffiliationRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/user-affiliation.component').then(m => m.UserAffiliationComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/user-affiliation-detail.component').then(m => m.UserAffiliationDetailComponent),
    resolve: {
      userAffiliation: UserAffiliationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/user-affiliation-update.component').then(m => m.UserAffiliationUpdateComponent),
    resolve: {
      userAffiliation: UserAffiliationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/user-affiliation-update.component').then(m => m.UserAffiliationUpdateComponent),
    resolve: {
      userAffiliation: UserAffiliationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default userAffiliationRoute;
