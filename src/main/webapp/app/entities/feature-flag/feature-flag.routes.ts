import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import FeatureFlagResolve from './route/feature-flag-routing-resolve.service';

const featureFlagRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/feature-flag.component').then(m => m.FeatureFlagComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/feature-flag-detail.component').then(m => m.FeatureFlagDetailComponent),
    resolve: {
      featureFlag: FeatureFlagResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/feature-flag-update.component').then(m => m.FeatureFlagUpdateComponent),
    resolve: {
      featureFlag: FeatureFlagResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/feature-flag-update.component').then(m => m.FeatureFlagUpdateComponent),
    resolve: {
      featureFlag: FeatureFlagResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default featureFlagRoute;
