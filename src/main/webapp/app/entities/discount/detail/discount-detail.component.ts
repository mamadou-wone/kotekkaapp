import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IDiscount } from '../discount.model';

@Component({
  standalone: true,
  selector: 'jhi-discount-detail',
  templateUrl: './discount-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class DiscountDetailComponent {
  discount = input<IDiscount | null>(null);

  previousState(): void {
    window.history.back();
  }
}
