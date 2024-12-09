import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../money-request.test-samples';

import { MoneyRequestFormService } from './money-request-form.service';

describe('MoneyRequest Form Service', () => {
  let service: MoneyRequestFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MoneyRequestFormService);
  });

  describe('Service methods', () => {
    describe('createMoneyRequestFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMoneyRequestFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            uuid: expect.any(Object),
            status: expect.any(Object),
            otherHolder: expect.any(Object),
            amount: expect.any(Object),
            description: expect.any(Object),
            currency: expect.any(Object),
            walletHolder: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
          }),
        );
      });

      it('passing IMoneyRequest should create a new form with FormGroup', () => {
        const formGroup = service.createMoneyRequestFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            uuid: expect.any(Object),
            status: expect.any(Object),
            otherHolder: expect.any(Object),
            amount: expect.any(Object),
            description: expect.any(Object),
            currency: expect.any(Object),
            walletHolder: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
          }),
        );
      });
    });

    describe('getMoneyRequest', () => {
      it('should return NewMoneyRequest for default MoneyRequest initial value', () => {
        const formGroup = service.createMoneyRequestFormGroup(sampleWithNewData);

        const moneyRequest = service.getMoneyRequest(formGroup) as any;

        expect(moneyRequest).toMatchObject(sampleWithNewData);
      });

      it('should return NewMoneyRequest for empty MoneyRequest initial value', () => {
        const formGroup = service.createMoneyRequestFormGroup();

        const moneyRequest = service.getMoneyRequest(formGroup) as any;

        expect(moneyRequest).toMatchObject({});
      });

      it('should return IMoneyRequest', () => {
        const formGroup = service.createMoneyRequestFormGroup(sampleWithRequiredData);

        const moneyRequest = service.getMoneyRequest(formGroup) as any;

        expect(moneyRequest).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMoneyRequest should not enable id FormControl', () => {
        const formGroup = service.createMoneyRequestFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMoneyRequest should disable id FormControl', () => {
        const formGroup = service.createMoneyRequestFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
