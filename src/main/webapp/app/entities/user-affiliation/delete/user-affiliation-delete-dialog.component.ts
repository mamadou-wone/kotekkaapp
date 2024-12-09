import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IUserAffiliation } from '../user-affiliation.model';
import { UserAffiliationService } from '../service/user-affiliation.service';

@Component({
  standalone: true,
  templateUrl: './user-affiliation-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class UserAffiliationDeleteDialogComponent {
  userAffiliation?: IUserAffiliation;

  protected userAffiliationService = inject(UserAffiliationService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.userAffiliationService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
