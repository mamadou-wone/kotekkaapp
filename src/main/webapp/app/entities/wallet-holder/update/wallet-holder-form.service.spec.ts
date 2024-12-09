import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../wallet-holder.test-samples';

import { WalletHolderFormService } from './wallet-holder-form.service';

describe('WalletHolder Form Service', () => {
  let service: WalletHolderFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(WalletHolderFormService);
  });

  describe('Service methods', () => {
    describe('createWalletHolderFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createWalletHolderFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            uuid: expect.any(Object),
            type: expect.any(Object),
            status: expect.any(Object),
            phoneNumber: expect.any(Object),
            network: expect.any(Object),
            tag: expect.any(Object),
            firstName: expect.any(Object),
            lastName: expect.any(Object),
            address: expect.any(Object),
            city: expect.any(Object),
            country: expect.any(Object),
            postalCode: expect.any(Object),
            onboarding: expect.any(Object),
            externalId: expect.any(Object),
            email: expect.any(Object),
            dateOfBirth: expect.any(Object),
            sex: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
            loginStatus: expect.any(Object),
            user: expect.any(Object),
          }),
        );
      });

      it('passing IWalletHolder should create a new form with FormGroup', () => {
        const formGroup = service.createWalletHolderFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            uuid: expect.any(Object),
            type: expect.any(Object),
            status: expect.any(Object),
            phoneNumber: expect.any(Object),
            network: expect.any(Object),
            tag: expect.any(Object),
            firstName: expect.any(Object),
            lastName: expect.any(Object),
            address: expect.any(Object),
            city: expect.any(Object),
            country: expect.any(Object),
            postalCode: expect.any(Object),
            onboarding: expect.any(Object),
            externalId: expect.any(Object),
            email: expect.any(Object),
            dateOfBirth: expect.any(Object),
            sex: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
            loginStatus: expect.any(Object),
            user: expect.any(Object),
          }),
        );
      });
    });

    describe('getWalletHolder', () => {
      it('should return NewWalletHolder for default WalletHolder initial value', () => {
        const formGroup = service.createWalletHolderFormGroup(sampleWithNewData);

        const walletHolder = service.getWalletHolder(formGroup) as any;

        expect(walletHolder).toMatchObject(sampleWithNewData);
      });

      it('should return NewWalletHolder for empty WalletHolder initial value', () => {
        const formGroup = service.createWalletHolderFormGroup();

        const walletHolder = service.getWalletHolder(formGroup) as any;

        expect(walletHolder).toMatchObject({});
      });

      it('should return IWalletHolder', () => {
        const formGroup = service.createWalletHolderFormGroup(sampleWithRequiredData);

        const walletHolder = service.getWalletHolder(formGroup) as any;

        expect(walletHolder).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IWalletHolder should not enable id FormControl', () => {
        const formGroup = service.createWalletHolderFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewWalletHolder should disable id FormControl', () => {
        const formGroup = service.createWalletHolderFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
