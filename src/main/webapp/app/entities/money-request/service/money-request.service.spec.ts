import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IMoneyRequest } from '../money-request.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../money-request.test-samples';

import { MoneyRequestService, RestMoneyRequest } from './money-request.service';

const requireRestSample: RestMoneyRequest = {
  ...sampleWithRequiredData,
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
  lastModifiedDate: sampleWithRequiredData.lastModifiedDate?.toJSON(),
};

describe('MoneyRequest Service', () => {
  let service: MoneyRequestService;
  let httpMock: HttpTestingController;
  let expectedResult: IMoneyRequest | IMoneyRequest[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(MoneyRequestService);
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

    it('should create a MoneyRequest', () => {
      const moneyRequest = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(moneyRequest).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a MoneyRequest', () => {
      const moneyRequest = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(moneyRequest).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a MoneyRequest', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of MoneyRequest', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a MoneyRequest', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addMoneyRequestToCollectionIfMissing', () => {
      it('should add a MoneyRequest to an empty array', () => {
        const moneyRequest: IMoneyRequest = sampleWithRequiredData;
        expectedResult = service.addMoneyRequestToCollectionIfMissing([], moneyRequest);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(moneyRequest);
      });

      it('should not add a MoneyRequest to an array that contains it', () => {
        const moneyRequest: IMoneyRequest = sampleWithRequiredData;
        const moneyRequestCollection: IMoneyRequest[] = [
          {
            ...moneyRequest,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMoneyRequestToCollectionIfMissing(moneyRequestCollection, moneyRequest);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a MoneyRequest to an array that doesn't contain it", () => {
        const moneyRequest: IMoneyRequest = sampleWithRequiredData;
        const moneyRequestCollection: IMoneyRequest[] = [sampleWithPartialData];
        expectedResult = service.addMoneyRequestToCollectionIfMissing(moneyRequestCollection, moneyRequest);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(moneyRequest);
      });

      it('should add only unique MoneyRequest to an array', () => {
        const moneyRequestArray: IMoneyRequest[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const moneyRequestCollection: IMoneyRequest[] = [sampleWithRequiredData];
        expectedResult = service.addMoneyRequestToCollectionIfMissing(moneyRequestCollection, ...moneyRequestArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const moneyRequest: IMoneyRequest = sampleWithRequiredData;
        const moneyRequest2: IMoneyRequest = sampleWithPartialData;
        expectedResult = service.addMoneyRequestToCollectionIfMissing([], moneyRequest, moneyRequest2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(moneyRequest);
        expect(expectedResult).toContain(moneyRequest2);
      });

      it('should accept null and undefined values', () => {
        const moneyRequest: IMoneyRequest = sampleWithRequiredData;
        expectedResult = service.addMoneyRequestToCollectionIfMissing([], null, moneyRequest, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(moneyRequest);
      });

      it('should return initial array if no MoneyRequest is added', () => {
        const moneyRequestCollection: IMoneyRequest[] = [sampleWithRequiredData];
        expectedResult = service.addMoneyRequestToCollectionIfMissing(moneyRequestCollection, undefined, null);
        expect(expectedResult).toEqual(moneyRequestCollection);
      });
    });

    describe('compareMoneyRequest', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMoneyRequest(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareMoneyRequest(entity1, entity2);
        const compareResult2 = service.compareMoneyRequest(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareMoneyRequest(entity1, entity2);
        const compareResult2 = service.compareMoneyRequest(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareMoneyRequest(entity1, entity2);
        const compareResult2 = service.compareMoneyRequest(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
