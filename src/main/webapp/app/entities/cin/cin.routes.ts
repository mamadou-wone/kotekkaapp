import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import CinResolve from './route/cin-routing-resolve.service';

const cinRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/cin.component').then(m => m.CinComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/cin-detail.component').then(m => m.CinDetailComponent),
    resolve: {
      cin: CinResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/cin-update.component').then(m => m.CinUpdateComponent),
    resolve: {
      cin: CinResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/cin-update.component').then(m => m.CinUpdateComponent),
    resolve: {
      cin: CinResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default cinRoute;
