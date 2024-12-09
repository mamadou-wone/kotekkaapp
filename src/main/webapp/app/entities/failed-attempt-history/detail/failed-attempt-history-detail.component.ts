import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IFailedAttemptHistory } from '../failed-attempt-history.model';

@Component({
  standalone: true,
  selector: 'jhi-failed-attempt-history-detail',
  templateUrl: './failed-attempt-history-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class FailedAttemptHistoryDetailComponent {
  failedAttemptHistory = input<IFailedAttemptHistory | null>(null);

  previousState(): void {
    window.history.back();
  }
}
