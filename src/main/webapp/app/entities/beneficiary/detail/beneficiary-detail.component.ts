import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IBeneficiary } from '../beneficiary.model';

@Component({
  standalone: true,
  selector: 'jhi-beneficiary-detail',
  templateUrl: './beneficiary-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class BeneficiaryDetailComponent {
  beneficiary = input<IBeneficiary | null>(null);

  previousState(): void {
    window.history.back();
  }
}
