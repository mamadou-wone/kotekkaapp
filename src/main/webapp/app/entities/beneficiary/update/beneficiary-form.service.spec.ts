import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../beneficiary.test-samples';

import { BeneficiaryFormService } from './beneficiary-form.service';

describe('Beneficiary Form Service', () => {
  let service: BeneficiaryFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BeneficiaryFormService);
  });

  describe('Service methods', () => {
    describe('createBeneficiaryFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createBeneficiaryFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            uuid: expect.any(Object),
            status: expect.any(Object),
            firstName: expect.any(Object),
            lastName: expect.any(Object),
            cin: expect.any(Object),
            iban: expect.any(Object),
            phoneNumber: expect.any(Object),
            email: expect.any(Object),
            walletHolder: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
          }),
        );
      });

      it('passing IBeneficiary should create a new form with FormGroup', () => {
        const formGroup = service.createBeneficiaryFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            uuid: expect.any(Object),
            status: expect.any(Object),
            firstName: expect.any(Object),
            lastName: expect.any(Object),
            cin: expect.any(Object),
            iban: expect.any(Object),
            phoneNumber: expect.any(Object),
            email: expect.any(Object),
            walletHolder: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
          }),
        );
      });
    });

    describe('getBeneficiary', () => {
      it('should return NewBeneficiary for default Beneficiary initial value', () => {
        const formGroup = service.createBeneficiaryFormGroup(sampleWithNewData);

        const beneficiary = service.getBeneficiary(formGroup) as any;

        expect(beneficiary).toMatchObject(sampleWithNewData);
      });

      it('should return NewBeneficiary for empty Beneficiary initial value', () => {
        const formGroup = service.createBeneficiaryFormGroup();

        const beneficiary = service.getBeneficiary(formGroup) as any;

        expect(beneficiary).toMatchObject({});
      });

      it('should return IBeneficiary', () => {
        const formGroup = service.createBeneficiaryFormGroup(sampleWithRequiredData);

        const beneficiary = service.getBeneficiary(formGroup) as any;

        expect(beneficiary).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IBeneficiary should not enable id FormControl', () => {
        const formGroup = service.createBeneficiaryFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewBeneficiary should disable id FormControl', () => {
        const formGroup = service.createBeneficiaryFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
