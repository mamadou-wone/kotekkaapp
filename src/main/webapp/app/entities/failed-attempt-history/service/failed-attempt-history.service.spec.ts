import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IFailedAttemptHistory } from '../failed-attempt-history.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../failed-attempt-history.test-samples';

import { FailedAttemptHistoryService, RestFailedAttemptHistory } from './failed-attempt-history.service';

const requireRestSample: RestFailedAttemptHistory = {
  ...sampleWithRequiredData,
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
};

describe('FailedAttemptHistory Service', () => {
  let service: FailedAttemptHistoryService;
  let httpMock: HttpTestingController;
  let expectedResult: IFailedAttemptHistory | IFailedAttemptHistory[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(FailedAttemptHistoryService);
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

    it('should create a FailedAttemptHistory', () => {
      const failedAttemptHistory = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(failedAttemptHistory).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FailedAttemptHistory', () => {
      const failedAttemptHistory = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(failedAttemptHistory).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FailedAttemptHistory', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FailedAttemptHistory', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a FailedAttemptHistory', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addFailedAttemptHistoryToCollectionIfMissing', () => {
      it('should add a FailedAttemptHistory to an empty array', () => {
        const failedAttemptHistory: IFailedAttemptHistory = sampleWithRequiredData;
        expectedResult = service.addFailedAttemptHistoryToCollectionIfMissing([], failedAttemptHistory);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(failedAttemptHistory);
      });

      it('should not add a FailedAttemptHistory to an array that contains it', () => {
        const failedAttemptHistory: IFailedAttemptHistory = sampleWithRequiredData;
        const failedAttemptHistoryCollection: IFailedAttemptHistory[] = [
          {
            ...failedAttemptHistory,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFailedAttemptHistoryToCollectionIfMissing(failedAttemptHistoryCollection, failedAttemptHistory);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FailedAttemptHistory to an array that doesn't contain it", () => {
        const failedAttemptHistory: IFailedAttemptHistory = sampleWithRequiredData;
        const failedAttemptHistoryCollection: IFailedAttemptHistory[] = [sampleWithPartialData];
        expectedResult = service.addFailedAttemptHistoryToCollectionIfMissing(failedAttemptHistoryCollection, failedAttemptHistory);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(failedAttemptHistory);
      });

      it('should add only unique FailedAttemptHistory to an array', () => {
        const failedAttemptHistoryArray: IFailedAttemptHistory[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const failedAttemptHistoryCollection: IFailedAttemptHistory[] = [sampleWithRequiredData];
        expectedResult = service.addFailedAttemptHistoryToCollectionIfMissing(failedAttemptHistoryCollection, ...failedAttemptHistoryArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const failedAttemptHistory: IFailedAttemptHistory = sampleWithRequiredData;
        const failedAttemptHistory2: IFailedAttemptHistory = sampleWithPartialData;
        expectedResult = service.addFailedAttemptHistoryToCollectionIfMissing([], failedAttemptHistory, failedAttemptHistory2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(failedAttemptHistory);
        expect(expectedResult).toContain(failedAttemptHistory2);
      });

      it('should accept null and undefined values', () => {
        const failedAttemptHistory: IFailedAttemptHistory = sampleWithRequiredData;
        expectedResult = service.addFailedAttemptHistoryToCollectionIfMissing([], null, failedAttemptHistory, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(failedAttemptHistory);
      });

      it('should return initial array if no FailedAttemptHistory is added', () => {
        const failedAttemptHistoryCollection: IFailedAttemptHistory[] = [sampleWithRequiredData];
        expectedResult = service.addFailedAttemptHistoryToCollectionIfMissing(failedAttemptHistoryCollection, undefined, null);
        expect(expectedResult).toEqual(failedAttemptHistoryCollection);
      });
    });

    describe('compareFailedAttemptHistory', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFailedAttemptHistory(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareFailedAttemptHistory(entity1, entity2);
        const compareResult2 = service.compareFailedAttemptHistory(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareFailedAttemptHistory(entity1, entity2);
        const compareResult2 = service.compareFailedAttemptHistory(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareFailedAttemptHistory(entity1, entity2);
        const compareResult2 = service.compareFailedAttemptHistory(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
