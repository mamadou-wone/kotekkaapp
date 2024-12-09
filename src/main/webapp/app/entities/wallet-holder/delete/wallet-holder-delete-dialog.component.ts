import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IWalletHolder } from '../wallet-holder.model';
import { WalletHolderService } from '../service/wallet-holder.service';

@Component({
  standalone: true,
  templateUrl: './wallet-holder-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class WalletHolderDeleteDialogComponent {
  walletHolder?: IWalletHolder;

  protected walletHolderService = inject(WalletHolderService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.walletHolderService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
