import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IIncomingCall } from '../incoming-call.model';
import { IncomingCallService } from '../service/incoming-call.service';

@Component({
  standalone: true,
  templateUrl: './incoming-call-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class IncomingCallDeleteDialogComponent {
  incomingCall?: IIncomingCall;

  protected incomingCallService = inject(IncomingCallService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.incomingCallService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
