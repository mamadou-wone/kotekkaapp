import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../feature-flag.test-samples';

import { FeatureFlagFormService } from './feature-flag-form.service';

describe('FeatureFlag Form Service', () => {
  let service: FeatureFlagFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FeatureFlagFormService);
  });

  describe('Service methods', () => {
    describe('createFeatureFlagFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFeatureFlagFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            enabled: expect.any(Object),
            description: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            updatedBy: expect.any(Object),
            updatedDate: expect.any(Object),
          }),
        );
      });

      it('passing IFeatureFlag should create a new form with FormGroup', () => {
        const formGroup = service.createFeatureFlagFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            enabled: expect.any(Object),
            description: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            updatedBy: expect.any(Object),
            updatedDate: expect.any(Object),
          }),
        );
      });
    });

    describe('getFeatureFlag', () => {
      it('should return NewFeatureFlag for default FeatureFlag initial value', () => {
        const formGroup = service.createFeatureFlagFormGroup(sampleWithNewData);

        const featureFlag = service.getFeatureFlag(formGroup) as any;

        expect(featureFlag).toMatchObject(sampleWithNewData);
      });

      it('should return NewFeatureFlag for empty FeatureFlag initial value', () => {
        const formGroup = service.createFeatureFlagFormGroup();

        const featureFlag = service.getFeatureFlag(formGroup) as any;

        expect(featureFlag).toMatchObject({});
      });

      it('should return IFeatureFlag', () => {
        const formGroup = service.createFeatureFlagFormGroup(sampleWithRequiredData);

        const featureFlag = service.getFeatureFlag(formGroup) as any;

        expect(featureFlag).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFeatureFlag should not enable id FormControl', () => {
        const formGroup = service.createFeatureFlagFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFeatureFlag should disable id FormControl', () => {
        const formGroup = service.createFeatureFlagFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
