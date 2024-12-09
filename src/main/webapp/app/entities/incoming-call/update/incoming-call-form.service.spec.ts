import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../incoming-call.test-samples';

import { IncomingCallFormService } from './incoming-call-form.service';

describe('IncomingCall Form Service', () => {
  let service: IncomingCallFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(IncomingCallFormService);
  });

  describe('Service methods', () => {
    describe('createIncomingCallFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createIncomingCallFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            partner: expect.any(Object),
            api: expect.any(Object),
            method: expect.any(Object),
            requestHeaders: expect.any(Object),
            requestBody: expect.any(Object),
            createdDate: expect.any(Object),
            responseStatusCode: expect.any(Object),
            responseTime: expect.any(Object),
          }),
        );
      });

      it('passing IIncomingCall should create a new form with FormGroup', () => {
        const formGroup = service.createIncomingCallFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            partner: expect.any(Object),
            api: expect.any(Object),
            method: expect.any(Object),
            requestHeaders: expect.any(Object),
            requestBody: expect.any(Object),
            createdDate: expect.any(Object),
            responseStatusCode: expect.any(Object),
            responseTime: expect.any(Object),
          }),
        );
      });
    });

    describe('getIncomingCall', () => {
      it('should return NewIncomingCall for default IncomingCall initial value', () => {
        const formGroup = service.createIncomingCallFormGroup(sampleWithNewData);

        const incomingCall = service.getIncomingCall(formGroup) as any;

        expect(incomingCall).toMatchObject(sampleWithNewData);
      });

      it('should return NewIncomingCall for empty IncomingCall initial value', () => {
        const formGroup = service.createIncomingCallFormGroup();

        const incomingCall = service.getIncomingCall(formGroup) as any;

        expect(incomingCall).toMatchObject({});
      });

      it('should return IIncomingCall', () => {
        const formGroup = service.createIncomingCallFormGroup(sampleWithRequiredData);

        const incomingCall = service.getIncomingCall(formGroup) as any;

        expect(incomingCall).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IIncomingCall should not enable id FormControl', () => {
        const formGroup = service.createIncomingCallFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewIncomingCall should disable id FormControl', () => {
        const formGroup = service.createIncomingCallFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
