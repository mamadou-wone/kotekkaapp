import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IOneTimePassword } from '../one-time-password.model';
import { OneTimePasswordService } from '../service/one-time-password.service';

@Component({
  standalone: true,
  templateUrl: './one-time-password-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class OneTimePasswordDeleteDialogComponent {
  oneTimePassword?: IOneTimePassword;

  protected oneTimePasswordService = inject(OneTimePasswordService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.oneTimePasswordService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
