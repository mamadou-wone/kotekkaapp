import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { Partner } from 'app/entities/enumerations/partner.model';
import { HttpMethod } from 'app/entities/enumerations/http-method.model';
import { PartnerCallService } from '../service/partner-call.service';
import { IPartnerCall } from '../partner-call.model';
import { PartnerCallFormGroup, PartnerCallFormService } from './partner-call-form.service';

@Component({
  standalone: true,
  selector: 'jhi-partner-call-update',
  templateUrl: './partner-call-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PartnerCallUpdateComponent implements OnInit {
  isSaving = false;
  partnerCall: IPartnerCall | null = null;
  partnerValues = Object.keys(Partner);
  httpMethodValues = Object.keys(HttpMethod);

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected partnerCallService = inject(PartnerCallService);
  protected partnerCallFormService = inject(PartnerCallFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PartnerCallFormGroup = this.partnerCallFormService.createPartnerCallFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ partnerCall }) => {
      this.partnerCall = partnerCall;
      if (partnerCall) {
        this.updateForm(partnerCall);
      }
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('kotekkaappApp.error', { ...err, key: `error.file.${err.key}` })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const partnerCall = this.partnerCallFormService.getPartnerCall(this.editForm);
    if (partnerCall.id !== null) {
      this.subscribeToSaveResponse(this.partnerCallService.update(partnerCall));
    } else {
      this.subscribeToSaveResponse(this.partnerCallService.create(partnerCall));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPartnerCall>>): void {
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

  protected updateForm(partnerCall: IPartnerCall): void {
    this.partnerCall = partnerCall;
    this.partnerCallFormService.resetForm(this.editForm, partnerCall);
  }
}
