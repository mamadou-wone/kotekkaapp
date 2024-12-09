import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import UserAccessResolve from './route/user-access-routing-resolve.service';

const userAccessRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/user-access.component').then(m => m.UserAccessComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/user-access-detail.component').then(m => m.UserAccessDetailComponent),
    resolve: {
      userAccess: UserAccessResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/user-access-update.component').then(m => m.UserAccessUpdateComponent),
    resolve: {
      userAccess: UserAccessResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/user-access-update.component').then(m => m.UserAccessUpdateComponent),
    resolve: {
      userAccess: UserAccessResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default userAccessRoute;
