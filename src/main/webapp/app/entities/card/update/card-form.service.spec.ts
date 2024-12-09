import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../card.test-samples';

import { CardFormService } from './card-form.service';

describe('Card Form Service', () => {
  let service: CardFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CardFormService);
  });

  describe('Service methods', () => {
    describe('createCardFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCardFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            uuid: expect.any(Object),
            status: expect.any(Object),
            label: expect.any(Object),
            maskedPan: expect.any(Object),
            cardHolderName: expect.any(Object),
            token: expect.any(Object),
            expiryYear: expect.any(Object),
            expiryMonth: expect.any(Object),
            rnd: expect.any(Object),
            hash: expect.any(Object),
            walletHolder: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
          }),
        );
      });

      it('passing ICard should create a new form with FormGroup', () => {
        const formGroup = service.createCardFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            uuid: expect.any(Object),
            status: expect.any(Object),
            label: expect.any(Object),
            maskedPan: expect.any(Object),
            cardHolderName: expect.any(Object),
            token: expect.any(Object),
            expiryYear: expect.any(Object),
            expiryMonth: expect.any(Object),
            rnd: expect.any(Object),
            hash: expect.any(Object),
            walletHolder: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
          }),
        );
      });
    });

    describe('getCard', () => {
      it('should return NewCard for default Card initial value', () => {
        const formGroup = service.createCardFormGroup(sampleWithNewData);

        const card = service.getCard(formGroup) as any;

        expect(card).toMatchObject(sampleWithNewData);
      });

      it('should return NewCard for empty Card initial value', () => {
        const formGroup = service.createCardFormGroup();

        const card = service.getCard(formGroup) as any;

        expect(card).toMatchObject({});
      });

      it('should return ICard', () => {
        const formGroup = service.createCardFormGroup(sampleWithRequiredData);

        const card = service.getCard(formGroup) as any;

        expect(card).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICard should not enable id FormControl', () => {
        const formGroup = service.createCardFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCard should disable id FormControl', () => {
        const formGroup = service.createCardFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
