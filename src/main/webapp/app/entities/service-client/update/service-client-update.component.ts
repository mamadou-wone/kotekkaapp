import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AccountType } from 'app/entities/enumerations/account-type.model';
import { DefaultStatus } from 'app/entities/enumerations/default-status.model';
import { IServiceClient } from '../service-client.model';
import { ServiceClientService } from '../service/service-client.service';
import { ServiceClientFormGroup, ServiceClientFormService } from './service-client-form.service';

@Component({
  standalone: true,
  selector: 'jhi-service-client-update',
  templateUrl: './service-client-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ServiceClientUpdateComponent implements OnInit {
  isSaving = false;
  serviceClient: IServiceClient | null = null;
  accountTypeValues = Object.keys(AccountType);
  defaultStatusValues = Object.keys(DefaultStatus);

  protected serviceClientService = inject(ServiceClientService);
  protected serviceClientFormService = inject(ServiceClientFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ServiceClientFormGroup = this.serviceClientFormService.createServiceClientFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ serviceClient }) => {
      this.serviceClient = serviceClient;
      if (serviceClient) {
        this.updateForm(serviceClient);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const serviceClient = this.serviceClientFormService.getServiceClient(this.editForm);
    if (serviceClient.id !== null) {
      this.subscribeToSaveResponse(this.serviceClientService.update(serviceClient));
    } else {
      this.subscribeToSaveResponse(this.serviceClientService.create(serviceClient));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IServiceClient>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(serviceClient: IServiceClient): void {
    this.serviceClient = serviceClient;
    this.serviceClientFormService.resetForm(this.editForm, serviceClient);
  }
}
