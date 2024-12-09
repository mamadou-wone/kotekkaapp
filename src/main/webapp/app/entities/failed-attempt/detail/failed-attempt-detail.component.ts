import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IFailedAttempt } from '../failed-attempt.model';

@Component({
  standalone: true,
  selector: 'jhi-failed-attempt-detail',
  templateUrl: './failed-attempt-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class FailedAttemptDetailComponent {
  failedAttempt = input<IFailedAttempt | null>(null);

  previousState(): void {
    window.history.back();
  }
}
