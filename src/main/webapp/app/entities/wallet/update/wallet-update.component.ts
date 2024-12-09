import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { WalletType } from 'app/entities/enumerations/wallet-type.model';
import { WalletStatus } from 'app/entities/enumerations/wallet-status.model';
import { AccountLevel } from 'app/entities/enumerations/account-level.model';
import { WalletService } from '../service/wallet.service';
import { IWallet } from '../wallet.model';
import { WalletFormGroup, WalletFormService } from './wallet-form.service';

@Component({
  standalone: true,
  selector: 'jhi-wallet-update',
  templateUrl: './wallet-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class WalletUpdateComponent implements OnInit {
  isSaving = false;
  wallet: IWallet | null = null;
  walletTypeValues = Object.keys(WalletType);
  walletStatusValues = Object.keys(WalletStatus);
  accountLevelValues = Object.keys(AccountLevel);

  protected walletService = inject(WalletService);
  protected walletFormService = inject(WalletFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: WalletFormGroup = this.walletFormService.createWalletFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ wallet }) => {
      this.wallet = wallet;
      if (wallet) {
        this.updateForm(wallet);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const wallet = this.walletFormService.getWallet(this.editForm);
    if (wallet.id !== null) {
      this.subscribeToSaveResponse(this.walletService.update(wallet));
    } else {
      this.subscribeToSaveResponse(this.walletService.create(wallet));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWallet>>): void {
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

  protected updateForm(wallet: IWallet): void {
    this.wallet = wallet;
    this.walletFormService.resetForm(this.editForm, wallet);
  }
}
