import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IUserAccess } from '../user-access.model';
import { UserAccessService } from '../service/user-access.service';

@Component({
  standalone: true,
  templateUrl: './user-access-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class UserAccessDeleteDialogComponent {
  userAccess?: IUserAccess;

  protected userAccessService = inject(UserAccessService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.userAccessService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
