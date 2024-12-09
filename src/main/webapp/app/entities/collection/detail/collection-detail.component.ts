import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { ICollection } from '../collection.model';

@Component({
  standalone: true,
  selector: 'jhi-collection-detail',
  templateUrl: './collection-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class CollectionDetailComponent {
  collection = input<ICollection | null>(null);

  previousState(): void {
    window.history.back();
  }
}
