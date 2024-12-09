import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IServiceClient } from '../service-client.model';
import { ServiceClientService } from '../service/service-client.service';

@Component({
  standalone: true,
  templateUrl: './service-client-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ServiceClientDeleteDialogComponent {
  serviceClient?: IServiceClient;

  protected serviceClientService = inject(ServiceClientService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.serviceClientService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
