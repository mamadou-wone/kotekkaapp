import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IPartnerCall } from '../partner-call.model';
import { PartnerCallService } from '../service/partner-call.service';

@Component({
  standalone: true,
  templateUrl: './partner-call-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class PartnerCallDeleteDialogComponent {
  partnerCall?: IPartnerCall;

  protected partnerCallService = inject(PartnerCallService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.partnerCallService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
