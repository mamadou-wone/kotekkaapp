import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../cache-info.test-samples';

import { CacheInfoFormService } from './cache-info-form.service';

describe('CacheInfo Form Service', () => {
  let service: CacheInfoFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CacheInfoFormService);
  });

  describe('Service methods', () => {
    describe('createCacheInfoFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCacheInfoFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            walletHolder: expect.any(Object),
            key: expect.any(Object),
            value: expect.any(Object),
            createdDate: expect.any(Object),
          }),
        );
      });

      it('passing ICacheInfo should create a new form with FormGroup', () => {
        const formGroup = service.createCacheInfoFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            walletHolder: expect.any(Object),
            key: expect.any(Object),
            value: expect.any(Object),
            createdDate: expect.any(Object),
          }),
        );
      });
    });

    describe('getCacheInfo', () => {
      it('should return NewCacheInfo for default CacheInfo initial value', () => {
        const formGroup = service.createCacheInfoFormGroup(sampleWithNewData);

        const cacheInfo = service.getCacheInfo(formGroup) as any;

        expect(cacheInfo).toMatchObject(sampleWithNewData);
      });

      it('should return NewCacheInfo for empty CacheInfo initial value', () => {
        const formGroup = service.createCacheInfoFormGroup();

        const cacheInfo = service.getCacheInfo(formGroup) as any;

        expect(cacheInfo).toMatchObject({});
      });

      it('should return ICacheInfo', () => {
        const formGroup = service.createCacheInfoFormGroup(sampleWithRequiredData);

        const cacheInfo = service.getCacheInfo(formGroup) as any;

        expect(cacheInfo).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICacheInfo should not enable id FormControl', () => {
        const formGroup = service.createCacheInfoFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCacheInfo should disable id FormControl', () => {
        const formGroup = service.createCacheInfoFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
