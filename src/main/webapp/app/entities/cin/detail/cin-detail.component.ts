import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { ICin } from '../cin.model';

@Component({
  standalone: true,
  selector: 'jhi-cin-detail',
  templateUrl: './cin-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class CinDetailComponent {
  cin = input<ICin | null>(null);

  previousState(): void {
    window.history.back();
  }
}
