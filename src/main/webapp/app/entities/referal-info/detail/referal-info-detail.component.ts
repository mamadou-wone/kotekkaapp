import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IReferalInfo } from '../referal-info.model';

@Component({
  standalone: true,
  selector: 'jhi-referal-info-detail',
  templateUrl: './referal-info-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class ReferalInfoDetailComponent {
  referalInfo = input<IReferalInfo | null>(null);

  previousState(): void {
    window.history.back();
  }
}
