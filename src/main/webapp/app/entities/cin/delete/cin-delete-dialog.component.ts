import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ICin } from '../cin.model';
import { CinService } from '../service/cin.service';

@Component({
  standalone: true,
  templateUrl: './cin-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class CinDeleteDialogComponent {
  cin?: ICin;

  protected cinService = inject(CinService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.cinService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
