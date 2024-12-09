import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IUserAccess } from '../user-access.model';

@Component({
  standalone: true,
  selector: 'jhi-user-access-detail',
  templateUrl: './user-access-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class UserAccessDetailComponent {
  userAccess = input<IUserAccess | null>(null);

  previousState(): void {
    window.history.back();
  }
}
