import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ICacheInfo } from '../cache-info.model';
import { CacheInfoService } from '../service/cache-info.service';

@Component({
  standalone: true,
  templateUrl: './cache-info-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class CacheInfoDeleteDialogComponent {
  cacheInfo?: ICacheInfo;

  protected cacheInfoService = inject(CacheInfoService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.cacheInfoService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
