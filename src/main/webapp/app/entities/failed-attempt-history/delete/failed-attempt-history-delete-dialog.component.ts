import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IFailedAttemptHistory } from '../failed-attempt-history.model';
import { FailedAttemptHistoryService } from '../service/failed-attempt-history.service';

@Component({
  standalone: true,
  templateUrl: './failed-attempt-history-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class FailedAttemptHistoryDeleteDialogComponent {
  failedAttemptHistory?: IFailedAttemptHistory;

  protected failedAttemptHistoryService = inject(FailedAttemptHistoryService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.failedAttemptHistoryService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
