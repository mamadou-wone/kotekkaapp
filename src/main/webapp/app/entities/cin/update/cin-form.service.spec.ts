import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../cin.test-samples';

import { CinFormService } from './cin-form.service';

describe('Cin Form Service', () => {
  let service: CinFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CinFormService);
  });

  describe('Service methods', () => {
    describe('createCinFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCinFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            cinId: expect.any(Object),
            validityDate: expect.any(Object),
            birthDate: expect.any(Object),
            birthPlace: expect.any(Object),
            firstName: expect.any(Object),
            lastName: expect.any(Object),
            birthCity: expect.any(Object),
            fatherName: expect.any(Object),
            nationality: expect.any(Object),
            nationalityCode: expect.any(Object),
            issuingCountry: expect.any(Object),
            issuingCountryCode: expect.any(Object),
            motherName: expect.any(Object),
            civilRegister: expect.any(Object),
            sex: expect.any(Object),
            address: expect.any(Object),
            birthCityCode: expect.any(Object),
            walletHolder: expect.any(Object),
            createdDate: expect.any(Object),
          }),
        );
      });

      it('passing ICin should create a new form with FormGroup', () => {
        const formGroup = service.createCinFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            cinId: expect.any(Object),
            validityDate: expect.any(Object),
            birthDate: expect.any(Object),
            birthPlace: expect.any(Object),
            firstName: expect.any(Object),
            lastName: expect.any(Object),
            birthCity: expect.any(Object),
            fatherName: expect.any(Object),
            nationality: expect.any(Object),
            nationalityCode: expect.any(Object),
            issuingCountry: expect.any(Object),
            issuingCountryCode: expect.any(Object),
            motherName: expect.any(Object),
            civilRegister: expect.any(Object),
            sex: expect.any(Object),
            address: expect.any(Object),
            birthCityCode: expect.any(Object),
            walletHolder: expect.any(Object),
            createdDate: expect.any(Object),
          }),
        );
      });
    });

    describe('getCin', () => {
      it('should return NewCin for default Cin initial value', () => {
        const formGroup = service.createCinFormGroup(sampleWithNewData);

        const cin = service.getCin(formGroup) as any;

        expect(cin).toMatchObject(sampleWithNewData);
      });

      it('should return NewCin for empty Cin initial value', () => {
        const formGroup = service.createCinFormGroup();

        const cin = service.getCin(formGroup) as any;

        expect(cin).toMatchObject({});
      });

      it('should return ICin', () => {
        const formGroup = service.createCinFormGroup(sampleWithRequiredData);

        const cin = service.getCin(formGroup) as any;

        expect(cin).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICin should not enable id FormControl', () => {
        const formGroup = service.createCinFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCin should disable id FormControl', () => {
        const formGroup = service.createCinFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
