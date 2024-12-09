import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../failed-attempt-history.test-samples';

import { FailedAttemptHistoryFormService } from './failed-attempt-history-form.service';

describe('FailedAttemptHistory Form Service', () => {
  let service: FailedAttemptHistoryFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FailedAttemptHistoryFormService);
  });

  describe('Service methods', () => {
    describe('createFailedAttemptHistoryFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFailedAttemptHistoryFormGroup();

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

      it('passing IFailedAttemptHistory should create a new form with FormGroup', () => {
        const formGroup = service.createFailedAttemptHistoryFormGroup(sampleWithRequiredData);

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

    describe('getFailedAttemptHistory', () => {
      it('should return NewFailedAttemptHistory for default FailedAttemptHistory initial value', () => {
        const formGroup = service.createFailedAttemptHistoryFormGroup(sampleWithNewData);

        const failedAttemptHistory = service.getFailedAttemptHistory(formGroup) as any;

        expect(failedAttemptHistory).toMatchObject(sampleWithNewData);
      });

      it('should return NewFailedAttemptHistory for empty FailedAttemptHistory initial value', () => {
        const formGroup = service.createFailedAttemptHistoryFormGroup();

        const failedAttemptHistory = service.getFailedAttemptHistory(formGroup) as any;

        expect(failedAttemptHistory).toMatchObject({});
      });

      it('should return IFailedAttemptHistory', () => {
        const formGroup = service.createFailedAttemptHistoryFormGroup(sampleWithRequiredData);

        const failedAttemptHistory = service.getFailedAttemptHistory(formGroup) as any;

        expect(failedAttemptHistory).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFailedAttemptHistory should not enable id FormControl', () => {
        const formGroup = service.createFailedAttemptHistoryFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFailedAttemptHistory should disable id FormControl', () => {
        const formGroup = service.createFailedAttemptHistoryFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
