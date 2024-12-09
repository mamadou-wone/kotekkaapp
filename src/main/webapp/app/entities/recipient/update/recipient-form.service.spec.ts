import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../recipient.test-samples';

import { RecipientFormService } from './recipient-form.service';

describe('Recipient Form Service', () => {
  let service: RecipientFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RecipientFormService);
  });

  describe('Service methods', () => {
    describe('createRecipientFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createRecipientFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            uuid: expect.any(Object),
            status: expect.any(Object),
            firstName: expect.any(Object),
            lastName: expect.any(Object),
            phoneNumber: expect.any(Object),
            walletHolder: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
          }),
        );
      });

      it('passing IRecipient should create a new form with FormGroup', () => {
        const formGroup = service.createRecipientFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            uuid: expect.any(Object),
            status: expect.any(Object),
            firstName: expect.any(Object),
            lastName: expect.any(Object),
            phoneNumber: expect.any(Object),
            walletHolder: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
          }),
        );
      });
    });

    describe('getRecipient', () => {
      it('should return NewRecipient for default Recipient initial value', () => {
        const formGroup = service.createRecipientFormGroup(sampleWithNewData);

        const recipient = service.getRecipient(formGroup) as any;

        expect(recipient).toMatchObject(sampleWithNewData);
      });

      it('should return NewRecipient for empty Recipient initial value', () => {
        const formGroup = service.createRecipientFormGroup();

        const recipient = service.getRecipient(formGroup) as any;

        expect(recipient).toMatchObject({});
      });

      it('should return IRecipient', () => {
        const formGroup = service.createRecipientFormGroup(sampleWithRequiredData);

        const recipient = service.getRecipient(formGroup) as any;

        expect(recipient).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IRecipient should not enable id FormControl', () => {
        const formGroup = service.createRecipientFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewRecipient should disable id FormControl', () => {
        const formGroup = service.createRecipientFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
