import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../referal-info.test-samples';

import { ReferalInfoFormService } from './referal-info-form.service';

describe('ReferalInfo Form Service', () => {
  let service: ReferalInfoFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ReferalInfoFormService);
  });

  describe('Service methods', () => {
    describe('createReferalInfoFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createReferalInfoFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            uuid: expect.any(Object),
            referalCode: expect.any(Object),
            walletHolder: expect.any(Object),
            refered: expect.any(Object),
            status: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
          }),
        );
      });

      it('passing IReferalInfo should create a new form with FormGroup', () => {
        const formGroup = service.createReferalInfoFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            uuid: expect.any(Object),
            referalCode: expect.any(Object),
            walletHolder: expect.any(Object),
            refered: expect.any(Object),
            status: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
          }),
        );
      });
    });

    describe('getReferalInfo', () => {
      it('should return NewReferalInfo for default ReferalInfo initial value', () => {
        const formGroup = service.createReferalInfoFormGroup(sampleWithNewData);

        const referalInfo = service.getReferalInfo(formGroup) as any;

        expect(referalInfo).toMatchObject(sampleWithNewData);
      });

      it('should return NewReferalInfo for empty ReferalInfo initial value', () => {
        const formGroup = service.createReferalInfoFormGroup();

        const referalInfo = service.getReferalInfo(formGroup) as any;

        expect(referalInfo).toMatchObject({});
      });

      it('should return IReferalInfo', () => {
        const formGroup = service.createReferalInfoFormGroup(sampleWithRequiredData);

        const referalInfo = service.getReferalInfo(formGroup) as any;

        expect(referalInfo).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IReferalInfo should not enable id FormControl', () => {
        const formGroup = service.createReferalInfoFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewReferalInfo should disable id FormControl', () => {
        const formGroup = service.createReferalInfoFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
