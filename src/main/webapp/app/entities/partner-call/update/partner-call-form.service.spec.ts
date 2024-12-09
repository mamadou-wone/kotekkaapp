import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../partner-call.test-samples';

import { PartnerCallFormService } from './partner-call-form.service';

describe('PartnerCall Form Service', () => {
  let service: PartnerCallFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PartnerCallFormService);
  });

  describe('Service methods', () => {
    describe('createPartnerCallFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPartnerCallFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            partner: expect.any(Object),
            api: expect.any(Object),
            method: expect.any(Object),
            requestHeaders: expect.any(Object),
            requestBody: expect.any(Object),
            requestTime: expect.any(Object),
            responseStatusCode: expect.any(Object),
            responseHeaders: expect.any(Object),
            responseBody: expect.any(Object),
            responseTime: expect.any(Object),
            correlationId: expect.any(Object),
            queryParam: expect.any(Object),
          }),
        );
      });

      it('passing IPartnerCall should create a new form with FormGroup', () => {
        const formGroup = service.createPartnerCallFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            partner: expect.any(Object),
            api: expect.any(Object),
            method: expect.any(Object),
            requestHeaders: expect.any(Object),
            requestBody: expect.any(Object),
            requestTime: expect.any(Object),
            responseStatusCode: expect.any(Object),
            responseHeaders: expect.any(Object),
            responseBody: expect.any(Object),
            responseTime: expect.any(Object),
            correlationId: expect.any(Object),
            queryParam: expect.any(Object),
          }),
        );
      });
    });

    describe('getPartnerCall', () => {
      it('should return NewPartnerCall for default PartnerCall initial value', () => {
        const formGroup = service.createPartnerCallFormGroup(sampleWithNewData);

        const partnerCall = service.getPartnerCall(formGroup) as any;

        expect(partnerCall).toMatchObject(sampleWithNewData);
      });

      it('should return NewPartnerCall for empty PartnerCall initial value', () => {
        const formGroup = service.createPartnerCallFormGroup();

        const partnerCall = service.getPartnerCall(formGroup) as any;

        expect(partnerCall).toMatchObject({});
      });

      it('should return IPartnerCall', () => {
        const formGroup = service.createPartnerCallFormGroup(sampleWithRequiredData);

        const partnerCall = service.getPartnerCall(formGroup) as any;

        expect(partnerCall).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPartnerCall should not enable id FormControl', () => {
        const formGroup = service.createPartnerCallFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPartnerCall should disable id FormControl', () => {
        const formGroup = service.createPartnerCallFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
