import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IWalletHolder } from '../wallet-holder.model';

@Component({
  standalone: true,
  selector: 'jhi-wallet-holder-detail',
  templateUrl: './wallet-holder-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class WalletHolderDetailComponent {
  walletHolder = input<IWalletHolder | null>(null);

  previousState(): void {
    window.history.back();
  }
}
