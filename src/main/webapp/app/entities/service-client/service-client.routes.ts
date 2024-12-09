import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ServiceClientResolve from './route/service-client-routing-resolve.service';

const serviceClientRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/service-client.component').then(m => m.ServiceClientComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/service-client-detail.component').then(m => m.ServiceClientDetailComponent),
    resolve: {
      serviceClient: ServiceClientResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/service-client-update.component').then(m => m.ServiceClientUpdateComponent),
    resolve: {
      serviceClient: ServiceClientResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/service-client-update.component').then(m => m.ServiceClientUpdateComponent),
    resolve: {
      serviceClient: ServiceClientResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default serviceClientRoute;
