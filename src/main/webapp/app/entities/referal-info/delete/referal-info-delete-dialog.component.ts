import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IReferalInfo } from '../referal-info.model';
import { ReferalInfoService } from '../service/referal-info.service';

@Component({
  standalone: true,
  templateUrl: './referal-info-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ReferalInfoDeleteDialogComponent {
  referalInfo?: IReferalInfo;

  protected referalInfoService = inject(ReferalInfoService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.referalInfoService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
