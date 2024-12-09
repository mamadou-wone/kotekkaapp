import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'kotekkaappApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'beneficiary',
    data: { pageTitle: 'kotekkaappApp.beneficiary.home.title' },
    loadChildren: () => import('./beneficiary/beneficiary.routes'),
  },
  {
    path: 'cache-info',
    data: { pageTitle: 'kotekkaappApp.cacheInfo.home.title' },
    loadChildren: () => import('./cache-info/cache-info.routes'),
  },
  {
    path: 'card',
    data: { pageTitle: 'kotekkaappApp.card.home.title' },
    loadChildren: () => import('./card/card.routes'),
  },
  {
    path: 'cin',
    data: { pageTitle: 'kotekkaappApp.cin.home.title' },
    loadChildren: () => import('./cin/cin.routes'),
  },
  {
    path: 'device',
    data: { pageTitle: 'kotekkaappApp.device.home.title' },
    loadChildren: () => import('./device/device.routes'),
  },
  {
    path: 'failed-attempt',
    data: { pageTitle: 'kotekkaappApp.failedAttempt.home.title' },
    loadChildren: () => import('./failed-attempt/failed-attempt.routes'),
  },
  {
    path: 'failed-attempt-history',
    data: { pageTitle: 'kotekkaappApp.failedAttemptHistory.home.title' },
    loadChildren: () => import('./failed-attempt-history/failed-attempt-history.routes'),
  },
  {
    path: 'feature-flag',
    data: { pageTitle: 'kotekkaappApp.featureFlag.home.title' },
    loadChildren: () => import('./feature-flag/feature-flag.routes'),
  },
  {
    path: 'image',
    data: { pageTitle: 'kotekkaappApp.image.home.title' },
    loadChildren: () => import('./image/image.routes'),
  },
  {
    path: 'incoming-call',
    data: { pageTitle: 'kotekkaappApp.incomingCall.home.title' },
    loadChildren: () => import('./incoming-call/incoming-call.routes'),
  },
  {
    path: 'money-request',
    data: { pageTitle: 'kotekkaappApp.moneyRequest.home.title' },
    loadChildren: () => import('./money-request/money-request.routes'),
  },
  {
    path: 'notification',
    data: { pageTitle: 'kotekkaappApp.notification.home.title' },
    loadChildren: () => import('./notification/notification.routes'),
  },
  {
    path: 'one-time-password',
    data: { pageTitle: 'kotekkaappApp.oneTimePassword.home.title' },
    loadChildren: () => import('./one-time-password/one-time-password.routes'),
  },
  {
    path: 'organisation',
    data: { pageTitle: 'kotekkaappApp.organisation.home.title' },
    loadChildren: () => import('./organisation/organisation.routes'),
  },
  {
    path: 'partner-call',
    data: { pageTitle: 'kotekkaappApp.partnerCall.home.title' },
    loadChildren: () => import('./partner-call/partner-call.routes'),
  },
  {
    path: 'recipient',
    data: { pageTitle: 'kotekkaappApp.recipient.home.title' },
    loadChildren: () => import('./recipient/recipient.routes'),
  },
  {
    path: 'referal-info',
    data: { pageTitle: 'kotekkaappApp.referalInfo.home.title' },
    loadChildren: () => import('./referal-info/referal-info.routes'),
  },
  {
    path: 'service-client',
    data: { pageTitle: 'kotekkaappApp.serviceClient.home.title' },
    loadChildren: () => import('./service-client/service-client.routes'),
  },
  {
    path: 'transaction',
    data: { pageTitle: 'kotekkaappApp.transaction.home.title' },
    loadChildren: () => import('./transaction/transaction.routes'),
  },
  {
    path: 'user-access',
    data: { pageTitle: 'kotekkaappApp.userAccess.home.title' },
    loadChildren: () => import('./user-access/user-access.routes'),
  },
  {
    path: 'user-affiliation',
    data: { pageTitle: 'kotekkaappApp.userAffiliation.home.title' },
    loadChildren: () => import('./user-affiliation/user-affiliation.routes'),
  },
  {
    path: 'user-preference',
    data: { pageTitle: 'kotekkaappApp.userPreference.home.title' },
    loadChildren: () => import('./user-preference/user-preference.routes'),
  },
  {
    path: 'wallet',
    data: { pageTitle: 'kotekkaappApp.wallet.home.title' },
    loadChildren: () => import('./wallet/wallet.routes'),
  },
  {
    path: 'wallet-holder',
    data: { pageTitle: 'kotekkaappApp.walletHolder.home.title' },
    loadChildren: () => import('./wallet-holder/wallet-holder.routes'),
  },
  {
    path: 'product',
    data: { pageTitle: 'kotekkaappApp.product.home.title' },
    loadChildren: () => import('./product/product.routes'),
  },
  {
    path: 'order',
    data: { pageTitle: 'kotekkaappApp.order.home.title' },
    loadChildren: () => import('./order/order.routes'),
  },
  {
    path: 'discount',
    data: { pageTitle: 'kotekkaappApp.discount.home.title' },
    loadChildren: () => import('./discount/discount.routes'),
  },
  {
    path: 'cart',
    data: { pageTitle: 'kotekkaappApp.cart.home.title' },
    loadChildren: () => import('./cart/cart.routes'),
  },
  {
    path: 'cart-item',
    data: { pageTitle: 'kotekkaappApp.cartItem.home.title' },
    loadChildren: () => import('./cart-item/cart-item.routes'),
  },
  {
    path: 'audit-log',
    data: { pageTitle: 'kotekkaappApp.auditLog.home.title' },
    loadChildren: () => import('./audit-log/audit-log.routes'),
  },
  {
    path: 'review',
    data: { pageTitle: 'kotekkaappApp.review.home.title' },
    loadChildren: () => import('./review/review.routes'),
  },
  {
    path: 'category',
    data: { pageTitle: 'kotekkaappApp.category.home.title' },
    loadChildren: () => import('./category/category.routes'),
  },
  {
    path: 'collection',
    data: { pageTitle: 'kotekkaappApp.collection.home.title' },
    loadChildren: () => import('./collection/collection.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
