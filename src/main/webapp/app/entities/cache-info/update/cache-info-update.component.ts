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
import { CacheInfoService } from '../service/cache-info.service';
import { ICacheInfo } from '../cache-info.model';
import { CacheInfoFormGroup, CacheInfoFormService } from './cache-info-form.service';

@Component({
  standalone: true,
  selector: 'jhi-cache-info-update',
  templateUrl: './cache-info-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CacheInfoUpdateComponent implements OnInit {
  isSaving = false;
  cacheInfo: ICacheInfo | null = null;

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected cacheInfoService = inject(CacheInfoService);
  protected cacheInfoFormService = inject(CacheInfoFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CacheInfoFormGroup = this.cacheInfoFormService.createCacheInfoFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cacheInfo }) => {
      this.cacheInfo = cacheInfo;
      if (cacheInfo) {
        this.updateForm(cacheInfo);
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
    const cacheInfo = this.cacheInfoFormService.getCacheInfo(this.editForm);
    if (cacheInfo.id !== null) {
      this.subscribeToSaveResponse(this.cacheInfoService.update(cacheInfo));
    } else {
      this.subscribeToSaveResponse(this.cacheInfoService.create(cacheInfo));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICacheInfo>>): void {
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

  protected updateForm(cacheInfo: ICacheInfo): void {
    this.cacheInfo = cacheInfo;
    this.cacheInfoFormService.resetForm(this.editForm, cacheInfo);
  }
}
