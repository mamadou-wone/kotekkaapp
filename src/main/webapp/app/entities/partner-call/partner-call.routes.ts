import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import PartnerCallResolve from './route/partner-call-routing-resolve.service';

const partnerCallRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/partner-call.component').then(m => m.PartnerCallComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/partner-call-detail.component').then(m => m.PartnerCallDetailComponent),
    resolve: {
      partnerCall: PartnerCallResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/partner-call-update.component').then(m => m.PartnerCallUpdateComponent),
    resolve: {
      partnerCall: PartnerCallResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/partner-call-update.component').then(m => m.PartnerCallUpdateComponent),
    resolve: {
      partnerCall: PartnerCallResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default partnerCallRoute;
