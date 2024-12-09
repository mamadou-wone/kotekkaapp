import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IMoneyRequest } from '../money-request.model';

@Component({
  standalone: true,
  selector: 'jhi-money-request-detail',
  templateUrl: './money-request-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class MoneyRequestDetailComponent {
  moneyRequest = input<IMoneyRequest | null>(null);

  previousState(): void {
    window.history.back();
  }
}
