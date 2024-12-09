import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IRecipient } from '../recipient.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../recipient.test-samples';

import { RecipientService, RestRecipient } from './recipient.service';

const requireRestSample: RestRecipient = {
  ...sampleWithRequiredData,
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
  lastModifiedDate: sampleWithRequiredData.lastModifiedDate?.toJSON(),
};

describe('Recipient Service', () => {
  let service: RecipientService;
  let httpMock: HttpTestingController;
  let expectedResult: IRecipient | IRecipient[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(RecipientService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Recipient', () => {
      const recipient = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(recipient).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Recipient', () => {
      const recipient = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(recipient).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Recipient', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Recipient', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Recipient', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addRecipientToCollectionIfMissing', () => {
      it('should add a Recipient to an empty array', () => {
        const recipient: IRecipient = sampleWithRequiredData;
        expectedResult = service.addRecipientToCollectionIfMissing([], recipient);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(recipient);
      });

      it('should not add a Recipient to an array that contains it', () => {
        const recipient: IRecipient = sampleWithRequiredData;
        const recipientCollection: IRecipient[] = [
          {
            ...recipient,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addRecipientToCollectionIfMissing(recipientCollection, recipient);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Recipient to an array that doesn't contain it", () => {
        const recipient: IRecipient = sampleWithRequiredData;
        const recipientCollection: IRecipient[] = [sampleWithPartialData];
        expectedResult = service.addRecipientToCollectionIfMissing(recipientCollection, recipient);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(recipient);
      });

      it('should add only unique Recipient to an array', () => {
        const recipientArray: IRecipient[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const recipientCollection: IRecipient[] = [sampleWithRequiredData];
        expectedResult = service.addRecipientToCollectionIfMissing(recipientCollection, ...recipientArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const recipient: IRecipient = sampleWithRequiredData;
        const recipient2: IRecipient = sampleWithPartialData;
        expectedResult = service.addRecipientToCollectionIfMissing([], recipient, recipient2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(recipient);
        expect(expectedResult).toContain(recipient2);
      });

      it('should accept null and undefined values', () => {
        const recipient: IRecipient = sampleWithRequiredData;
        expectedResult = service.addRecipientToCollectionIfMissing([], null, recipient, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(recipient);
      });

      it('should return initial array if no Recipient is added', () => {
        const recipientCollection: IRecipient[] = [sampleWithRequiredData];
        expectedResult = service.addRecipientToCollectionIfMissing(recipientCollection, undefined, null);
        expect(expectedResult).toEqual(recipientCollection);
      });
    });

    describe('compareRecipient', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareRecipient(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareRecipient(entity1, entity2);
        const compareResult2 = service.compareRecipient(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareRecipient(entity1, entity2);
        const compareResult2 = service.compareRecipient(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareRecipient(entity1, entity2);
        const compareResult2 = service.compareRecipient(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
