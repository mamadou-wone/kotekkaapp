import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import BeneficiaryResolve from './route/beneficiary-routing-resolve.service';

const beneficiaryRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/beneficiary.component').then(m => m.BeneficiaryComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/beneficiary-detail.component').then(m => m.BeneficiaryDetailComponent),
    resolve: {
      beneficiary: BeneficiaryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/beneficiary-update.component').then(m => m.BeneficiaryUpdateComponent),
    resolve: {
      beneficiary: BeneficiaryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/beneficiary-update.component').then(m => m.BeneficiaryUpdateComponent),
    resolve: {
      beneficiary: BeneficiaryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default beneficiaryRoute;
