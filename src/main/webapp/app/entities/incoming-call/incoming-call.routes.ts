import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import IncomingCallResolve from './route/incoming-call-routing-resolve.service';

const incomingCallRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/incoming-call.component').then(m => m.IncomingCallComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/incoming-call-detail.component').then(m => m.IncomingCallDetailComponent),
    resolve: {
      incomingCall: IncomingCallResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/incoming-call-update.component').then(m => m.IncomingCallUpdateComponent),
    resolve: {
      incomingCall: IncomingCallResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/incoming-call-update.component').then(m => m.IncomingCallUpdateComponent),
    resolve: {
      incomingCall: IncomingCallResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default incomingCallRoute;
