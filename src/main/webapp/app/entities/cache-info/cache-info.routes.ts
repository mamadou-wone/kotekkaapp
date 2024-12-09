import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import CacheInfoResolve from './route/cache-info-routing-resolve.service';

const cacheInfoRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/cache-info.component').then(m => m.CacheInfoComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/cache-info-detail.component').then(m => m.CacheInfoDetailComponent),
    resolve: {
      cacheInfo: CacheInfoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/cache-info-update.component').then(m => m.CacheInfoUpdateComponent),
    resolve: {
      cacheInfo: CacheInfoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/cache-info-update.component').then(m => m.CacheInfoUpdateComponent),
    resolve: {
      cacheInfo: CacheInfoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default cacheInfoRoute;
