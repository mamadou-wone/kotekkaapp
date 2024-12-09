import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IFeatureFlag } from '../feature-flag.model';

@Component({
  standalone: true,
  selector: 'jhi-feature-flag-detail',
  templateUrl: './feature-flag-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class FeatureFlagDetailComponent {
  featureFlag = input<IFeatureFlag | null>(null);

  previousState(): void {
    window.history.back();
  }
}
