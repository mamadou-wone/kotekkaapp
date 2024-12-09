import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IServiceClient } from '../service-client.model';

@Component({
  standalone: true,
  selector: 'jhi-service-client-detail',
  templateUrl: './service-client-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class ServiceClientDetailComponent {
  serviceClient = input<IServiceClient | null>(null);

  previousState(): void {
    window.history.back();
  }
}
