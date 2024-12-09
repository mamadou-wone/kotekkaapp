import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../failed-attempt.test-samples';

import { FailedAttemptFormService } from './failed-attempt-form.service';

describe('FailedAttempt Form Service', () => {
  let service: FailedAttemptFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FailedAttemptFormService);
  });

  describe('Service methods', () => {
    describe('createFailedAttemptFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFailedAttemptFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            login: expect.any(Object),
            ipAddress: expect.any(Object),
            isAfterLock: expect.any(Object),
            app: expect.any(Object),
            action: expect.any(Object),
            device: expect.any(Object),
            createdDate: expect.any(Object),
            reason: expect.any(Object),
          }),
        );
      });

      it('passing IFailedAttempt should create a new form with FormGroup', () => {
        const formGroup = service.createFailedAttemptFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            login: expect.any(Object),
            ipAddress: expect.any(Object),
            isAfterLock: expect.any(Object),
            app: expect.any(Object),
            action: expect.any(Object),
            device: expect.any(Object),
            createdDate: expect.any(Object),
            reason: expect.any(Object),
          }),
        );
      });
    });

    describe('getFailedAttempt', () => {
      it('should return NewFailedAttempt for default FailedAttempt initial value', () => {
        const formGroup = service.createFailedAttemptFormGroup(sampleWithNewData);

        const failedAttempt = service.getFailedAttempt(formGroup) as any;

        expect(failedAttempt).toMatchObject(sampleWithNewData);
      });

      it('should return NewFailedAttempt for empty FailedAttempt initial value', () => {
        const formGroup = service.createFailedAttemptFormGroup();

        const failedAttempt = service.getFailedAttempt(formGroup) as any;

        expect(failedAttempt).toMatchObject({});
      });

      it('should return IFailedAttempt', () => {
        const formGroup = service.createFailedAttemptFormGroup(sampleWithRequiredData);

        const failedAttempt = service.getFailedAttempt(formGroup) as any;

        expect(failedAttempt).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFailedAttempt should not enable id FormControl', () => {
        const formGroup = service.createFailedAttemptFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFailedAttempt should disable id FormControl', () => {
        const formGroup = service.createFailedAttemptFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
