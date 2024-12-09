import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IBeneficiary } from '../beneficiary.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../beneficiary.test-samples';

import { BeneficiaryService, RestBeneficiary } from './beneficiary.service';

const requireRestSample: RestBeneficiary = {
  ...sampleWithRequiredData,
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
  lastModifiedDate: sampleWithRequiredData.lastModifiedDate?.toJSON(),
};

describe('Beneficiary Service', () => {
  let service: BeneficiaryService;
  let httpMock: HttpTestingController;
  let expectedResult: IBeneficiary | IBeneficiary[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(BeneficiaryService);
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

    it('should create a Beneficiary', () => {
      const beneficiary = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(beneficiary).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Beneficiary', () => {
      const beneficiary = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(beneficiary).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Beneficiary', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Beneficiary', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Beneficiary', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addBeneficiaryToCollectionIfMissing', () => {
      it('should add a Beneficiary to an empty array', () => {
        const beneficiary: IBeneficiary = sampleWithRequiredData;
        expectedResult = service.addBeneficiaryToCollectionIfMissing([], beneficiary);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(beneficiary);
      });

      it('should not add a Beneficiary to an array that contains it', () => {
        const beneficiary: IBeneficiary = sampleWithRequiredData;
        const beneficiaryCollection: IBeneficiary[] = [
          {
            ...beneficiary,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addBeneficiaryToCollectionIfMissing(beneficiaryCollection, beneficiary);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Beneficiary to an array that doesn't contain it", () => {
        const beneficiary: IBeneficiary = sampleWithRequiredData;
        const beneficiaryCollection: IBeneficiary[] = [sampleWithPartialData];
        expectedResult = service.addBeneficiaryToCollectionIfMissing(beneficiaryCollection, beneficiary);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(beneficiary);
      });

      it('should add only unique Beneficiary to an array', () => {
        const beneficiaryArray: IBeneficiary[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const beneficiaryCollection: IBeneficiary[] = [sampleWithRequiredData];
        expectedResult = service.addBeneficiaryToCollectionIfMissing(beneficiaryCollection, ...beneficiaryArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const beneficiary: IBeneficiary = sampleWithRequiredData;
        const beneficiary2: IBeneficiary = sampleWithPartialData;
        expectedResult = service.addBeneficiaryToCollectionIfMissing([], beneficiary, beneficiary2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(beneficiary);
        expect(expectedResult).toContain(beneficiary2);
      });

      it('should accept null and undefined values', () => {
        const beneficiary: IBeneficiary = sampleWithRequiredData;
        expectedResult = service.addBeneficiaryToCollectionIfMissing([], null, beneficiary, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(beneficiary);
      });

      it('should return initial array if no Beneficiary is added', () => {
        const beneficiaryCollection: IBeneficiary[] = [sampleWithRequiredData];
        expectedResult = service.addBeneficiaryToCollectionIfMissing(beneficiaryCollection, undefined, null);
        expect(expectedResult).toEqual(beneficiaryCollection);
      });
    });

    describe('compareBeneficiary', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareBeneficiary(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareBeneficiary(entity1, entity2);
        const compareResult2 = service.compareBeneficiary(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareBeneficiary(entity1, entity2);
        const compareResult2 = service.compareBeneficiary(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareBeneficiary(entity1, entity2);
        const compareResult2 = service.compareBeneficiary(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
