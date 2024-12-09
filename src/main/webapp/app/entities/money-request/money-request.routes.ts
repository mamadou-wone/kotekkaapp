import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import MoneyRequestResolve from './route/money-request-routing-resolve.service';

const moneyRequestRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/money-request.component').then(m => m.MoneyRequestComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/money-request-detail.component').then(m => m.MoneyRequestDetailComponent),
    resolve: {
      moneyRequest: MoneyRequestResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/money-request-update.component').then(m => m.MoneyRequestUpdateComponent),
    resolve: {
      moneyRequest: MoneyRequestResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/money-request-update.component').then(m => m.MoneyRequestUpdateComponent),
    resolve: {
      moneyRequest: MoneyRequestResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default moneyRequestRoute;
