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
import { IncomingCallService } from '../service/incoming-call.service';
import { IIncomingCall } from '../incoming-call.model';
import { IncomingCallFormGroup, IncomingCallFormService } from './incoming-call-form.service';

@Component({
  standalone: true,
  selector: 'jhi-incoming-call-update',
  templateUrl: './incoming-call-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class IncomingCallUpdateComponent implements OnInit {
  isSaving = false;
  incomingCall: IIncomingCall | null = null;
  partnerValues = Object.keys(Partner);
  httpMethodValues = Object.keys(HttpMethod);

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected incomingCallService = inject(IncomingCallService);
  protected incomingCallFormService = inject(IncomingCallFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: IncomingCallFormGroup = this.incomingCallFormService.createIncomingCallFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ incomingCall }) => {
      this.incomingCall = incomingCall;
      if (incomingCall) {
        this.updateForm(incomingCall);
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
    const incomingCall = this.incomingCallFormService.getIncomingCall(this.editForm);
    if (incomingCall.id !== null) {
      this.subscribeToSaveResponse(this.incomingCallService.update(incomingCall));
    } else {
      this.subscribeToSaveResponse(this.incomingCallService.create(incomingCall));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IIncomingCall>>): void {
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

  protected updateForm(incomingCall: IIncomingCall): void {
    this.incomingCall = incomingCall;
    this.incomingCallFormService.resetForm(this.editForm, incomingCall);
  }
}
