import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IOrganisation } from '../organisation.model';

@Component({
  standalone: true,
  selector: 'jhi-organisation-detail',
  templateUrl: './organisation-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class OrganisationDetailComponent {
  organisation = input<IOrganisation | null>(null);

  previousState(): void {
    window.history.back();
  }
}
