import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IRecipient } from '../recipient.model';

@Component({
  standalone: true,
  selector: 'jhi-recipient-detail',
  templateUrl: './recipient-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class RecipientDetailComponent {
  recipient = input<IRecipient | null>(null);

  previousState(): void {
    window.history.back();
  }
}
