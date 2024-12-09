import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IOneTimePassword } from '../one-time-password.model';

@Component({
  standalone: true,
  selector: 'jhi-one-time-password-detail',
  templateUrl: './one-time-password-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class OneTimePasswordDetailComponent {
  oneTimePassword = input<IOneTimePassword | null>(null);

  previousState(): void {
    window.history.back();
  }
}
