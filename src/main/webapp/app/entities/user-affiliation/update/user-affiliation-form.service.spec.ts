import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../user-affiliation.test-samples';

import { UserAffiliationFormService } from './user-affiliation-form.service';

describe('UserAffiliation Form Service', () => {
  let service: UserAffiliationFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UserAffiliationFormService);
  });

  describe('Service methods', () => {
    describe('createUserAffiliationFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createUserAffiliationFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            walletHolder: expect.any(Object),
            affiliation: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
          }),
        );
      });

      it('passing IUserAffiliation should create a new form with FormGroup', () => {
        const formGroup = service.createUserAffiliationFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            walletHolder: expect.any(Object),
            affiliation: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
          }),
        );
      });
    });

    describe('getUserAffiliation', () => {
      it('should return NewUserAffiliation for default UserAffiliation initial value', () => {
        const formGroup = service.createUserAffiliationFormGroup(sampleWithNewData);

        const userAffiliation = service.getUserAffiliation(formGroup) as any;

        expect(userAffiliation).toMatchObject(sampleWithNewData);
      });

      it('should return NewUserAffiliation for empty UserAffiliation initial value', () => {
        const formGroup = service.createUserAffiliationFormGroup();

        const userAffiliation = service.getUserAffiliation(formGroup) as any;

        expect(userAffiliation).toMatchObject({});
      });

      it('should return IUserAffiliation', () => {
        const formGroup = service.createUserAffiliationFormGroup(sampleWithRequiredData);

        const userAffiliation = service.getUserAffiliation(formGroup) as any;

        expect(userAffiliation).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IUserAffiliation should not enable id FormControl', () => {
        const formGroup = service.createUserAffiliationFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewUserAffiliation should disable id FormControl', () => {
        const formGroup = service.createUserAffiliationFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
