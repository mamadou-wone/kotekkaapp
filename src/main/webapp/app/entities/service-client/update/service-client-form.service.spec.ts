import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../service-client.test-samples';

import { ServiceClientFormService } from './service-client-form.service';

describe('ServiceClient Form Service', () => {
  let service: ServiceClientFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ServiceClientFormService);
  });

  describe('Service methods', () => {
    describe('createServiceClientFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createServiceClientFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            clientId: expect.any(Object),
            type: expect.any(Object),
            apiKey: expect.any(Object),
            status: expect.any(Object),
            note: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedDate: expect.any(Object),
          }),
        );
      });

      it('passing IServiceClient should create a new form with FormGroup', () => {
        const formGroup = service.createServiceClientFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            clientId: expect.any(Object),
            type: expect.any(Object),
            apiKey: expect.any(Object),
            status: expect.any(Object),
            note: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedDate: expect.any(Object),
          }),
        );
      });
    });

    describe('getServiceClient', () => {
      it('should return NewServiceClient for default ServiceClient initial value', () => {
        const formGroup = service.createServiceClientFormGroup(sampleWithNewData);

        const serviceClient = service.getServiceClient(formGroup) as any;

        expect(serviceClient).toMatchObject(sampleWithNewData);
      });

      it('should return NewServiceClient for empty ServiceClient initial value', () => {
        const formGroup = service.createServiceClientFormGroup();

        const serviceClient = service.getServiceClient(formGroup) as any;

        expect(serviceClient).toMatchObject({});
      });

      it('should return IServiceClient', () => {
        const formGroup = service.createServiceClientFormGroup(sampleWithRequiredData);

        const serviceClient = service.getServiceClient(formGroup) as any;

        expect(serviceClient).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IServiceClient should not enable id FormControl', () => {
        const formGroup = service.createServiceClientFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewServiceClient should disable id FormControl', () => {
        const formGroup = service.createServiceClientFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
