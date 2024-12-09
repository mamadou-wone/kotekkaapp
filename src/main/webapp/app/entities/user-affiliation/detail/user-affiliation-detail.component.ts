import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IUserAffiliation } from '../user-affiliation.model';

@Component({
  standalone: true,
  selector: 'jhi-user-affiliation-detail',
  templateUrl: './user-affiliation-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class UserAffiliationDetailComponent {
  userAffiliation = input<IUserAffiliation | null>(null);

  previousState(): void {
    window.history.back();
  }
}
