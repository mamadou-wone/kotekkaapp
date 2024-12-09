import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import WalletHolderResolve from './route/wallet-holder-routing-resolve.service';

const walletHolderRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/wallet-holder.component').then(m => m.WalletHolderComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/wallet-holder-detail.component').then(m => m.WalletHolderDetailComponent),
    resolve: {
      walletHolder: WalletHolderResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/wallet-holder-update.component').then(m => m.WalletHolderUpdateComponent),
    resolve: {
      walletHolder: WalletHolderResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/wallet-holder-update.component').then(m => m.WalletHolderUpdateComponent),
    resolve: {
      walletHolder: WalletHolderResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default walletHolderRoute;
