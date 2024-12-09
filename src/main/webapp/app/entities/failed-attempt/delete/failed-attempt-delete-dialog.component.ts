import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IFailedAttempt } from '../failed-attempt.model';
import { FailedAttemptService } from '../service/failed-attempt.service';

@Component({
  standalone: true,
  templateUrl: './failed-attempt-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class FailedAttemptDeleteDialogComponent {
  failedAttempt?: IFailedAttempt;

  protected failedAttemptService = inject(FailedAttemptService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.failedAttemptService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
