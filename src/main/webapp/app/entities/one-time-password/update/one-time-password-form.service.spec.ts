import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../one-time-password.test-samples';

import { OneTimePasswordFormService } from './one-time-password-form.service';

describe('OneTimePassword Form Service', () => {
  let service: OneTimePasswordFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(OneTimePasswordFormService);
  });

  describe('Service methods', () => {
    describe('createOneTimePasswordFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createOneTimePasswordFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            uuid: expect.any(Object),
            user: expect.any(Object),
            code: expect.any(Object),
            status: expect.any(Object),
            expiry: expect.any(Object),
            createdDate: expect.any(Object),
          }),
        );
      });

      it('passing IOneTimePassword should create a new form with FormGroup', () => {
        const formGroup = service.createOneTimePasswordFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            uuid: expect.any(Object),
            user: expect.any(Object),
            code: expect.any(Object),
            status: expect.any(Object),
            expiry: expect.any(Object),
            createdDate: expect.any(Object),
          }),
        );
      });
    });

    describe('getOneTimePassword', () => {
      it('should return NewOneTimePassword for default OneTimePassword initial value', () => {
        const formGroup = service.createOneTimePasswordFormGroup(sampleWithNewData);

        const oneTimePassword = service.getOneTimePassword(formGroup) as any;

        expect(oneTimePassword).toMatchObject(sampleWithNewData);
      });

      it('should return NewOneTimePassword for empty OneTimePassword initial value', () => {
        const formGroup = service.createOneTimePasswordFormGroup();

        const oneTimePassword = service.getOneTimePassword(formGroup) as any;

        expect(oneTimePassword).toMatchObject({});
      });

      it('should return IOneTimePassword', () => {
        const formGroup = service.createOneTimePasswordFormGroup(sampleWithRequiredData);

        const oneTimePassword = service.getOneTimePassword(formGroup) as any;

        expect(oneTimePassword).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IOneTimePassword should not enable id FormControl', () => {
        const formGroup = service.createOneTimePasswordFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewOneTimePassword should disable id FormControl', () => {
        const formGroup = service.createOneTimePasswordFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
