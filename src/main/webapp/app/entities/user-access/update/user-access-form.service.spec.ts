import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../user-access.test-samples';

import { UserAccessFormService } from './user-access-form.service';

describe('UserAccess Form Service', () => {
  let service: UserAccessFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UserAccessFormService);
  });

  describe('Service methods', () => {
    describe('createUserAccessFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createUserAccessFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            login: expect.any(Object),
            ipAddress: expect.any(Object),
            device: expect.any(Object),
            createdDate: expect.any(Object),
          }),
        );
      });

      it('passing IUserAccess should create a new form with FormGroup', () => {
        const formGroup = service.createUserAccessFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            login: expect.any(Object),
            ipAddress: expect.any(Object),
            device: expect.any(Object),
            createdDate: expect.any(Object),
          }),
        );
      });
    });

    describe('getUserAccess', () => {
      it('should return NewUserAccess for default UserAccess initial value', () => {
        const formGroup = service.createUserAccessFormGroup(sampleWithNewData);

        const userAccess = service.getUserAccess(formGroup) as any;

        expect(userAccess).toMatchObject(sampleWithNewData);
      });

      it('should return NewUserAccess for empty UserAccess initial value', () => {
        const formGroup = service.createUserAccessFormGroup();

        const userAccess = service.getUserAccess(formGroup) as any;

        expect(userAccess).toMatchObject({});
      });

      it('should return IUserAccess', () => {
        const formGroup = service.createUserAccessFormGroup(sampleWithRequiredData);

        const userAccess = service.getUserAccess(formGroup) as any;

        expect(userAccess).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IUserAccess should not enable id FormControl', () => {
        const formGroup = service.createUserAccessFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewUserAccess should disable id FormControl', () => {
        const formGroup = service.createUserAccessFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
