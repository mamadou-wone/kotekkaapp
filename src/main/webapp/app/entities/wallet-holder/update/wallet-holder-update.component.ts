import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { AccountType } from 'app/entities/enumerations/account-type.model';
import { AccountStatus } from 'app/entities/enumerations/account-status.model';
import { Network } from 'app/entities/enumerations/network.model';
import { OnboardingStatus } from 'app/entities/enumerations/onboarding-status.model';
import { Sex } from 'app/entities/enumerations/sex.model';
import { LoginStatus } from 'app/entities/enumerations/login-status.model';
import { WalletHolderService } from '../service/wallet-holder.service';
import { IWalletHolder } from '../wallet-holder.model';
import { WalletHolderFormGroup, WalletHolderFormService } from './wallet-holder-form.service';

@Component({
  standalone: true,
  selector: 'jhi-wallet-holder-update',
  templateUrl: './wallet-holder-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class WalletHolderUpdateComponent implements OnInit {
  isSaving = false;
  walletHolder: IWalletHolder | null = null;
  accountTypeValues = Object.keys(AccountType);
  accountStatusValues = Object.keys(AccountStatus);
  networkValues = Object.keys(Network);
  onboardingStatusValues = Object.keys(OnboardingStatus);
  sexValues = Object.keys(Sex);
  loginStatusValues = Object.keys(LoginStatus);

  usersSharedCollection: IUser[] = [];

  protected walletHolderService = inject(WalletHolderService);
  protected walletHolderFormService = inject(WalletHolderFormService);
  protected userService = inject(UserService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: WalletHolderFormGroup = this.walletHolderFormService.createWalletHolderFormGroup();

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ walletHolder }) => {
      this.walletHolder = walletHolder;
      if (walletHolder) {
        this.updateForm(walletHolder);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const walletHolder = this.walletHolderFormService.getWalletHolder(this.editForm);
    if (walletHolder.id !== null) {
      this.subscribeToSaveResponse(this.walletHolderService.update(walletHolder));
    } else {
      this.subscribeToSaveResponse(this.walletHolderService.create(walletHolder));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWalletHolder>>): void {
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

  protected updateForm(walletHolder: IWalletHolder): void {
    this.walletHolder = walletHolder;
    this.walletHolderFormService.resetForm(this.editForm, walletHolder);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, walletHolder.user);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.walletHolder?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
