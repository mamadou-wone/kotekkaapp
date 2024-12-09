import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { ICard } from '../card.model';

@Component({
  standalone: true,
  selector: 'jhi-card-detail',
  templateUrl: './card-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class CardDetailComponent {
  card = input<ICard | null>(null);

  previousState(): void {
    window.history.back();
  }
}
