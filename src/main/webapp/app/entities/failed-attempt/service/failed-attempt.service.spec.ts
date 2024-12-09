import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IFailedAttempt } from '../failed-attempt.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../failed-attempt.test-samples';

import { FailedAttemptService, RestFailedAttempt } from './failed-attempt.service';

const requireRestSample: RestFailedAttempt = {
  ...sampleWithRequiredData,
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
};

describe('FailedAttempt Service', () => {
  let service: FailedAttemptService;
  let httpMock: HttpTestingController;
  let expectedResult: IFailedAttempt | IFailedAttempt[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(FailedAttemptService);
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

    it('should create a FailedAttempt', () => {
      const failedAttempt = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(failedAttempt).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FailedAttempt', () => {
      const failedAttempt = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(failedAttempt).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FailedAttempt', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FailedAttempt', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a FailedAttempt', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addFailedAttemptToCollectionIfMissing', () => {
      it('should add a FailedAttempt to an empty array', () => {
        const failedAttempt: IFailedAttempt = sampleWithRequiredData;
        expectedResult = service.addFailedAttemptToCollectionIfMissing([], failedAttempt);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(failedAttempt);
      });

      it('should not add a FailedAttempt to an array that contains it', () => {
        const failedAttempt: IFailedAttempt = sampleWithRequiredData;
        const failedAttemptCollection: IFailedAttempt[] = [
          {
            ...failedAttempt,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFailedAttemptToCollectionIfMissing(failedAttemptCollection, failedAttempt);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FailedAttempt to an array that doesn't contain it", () => {
        const failedAttempt: IFailedAttempt = sampleWithRequiredData;
        const failedAttemptCollection: IFailedAttempt[] = [sampleWithPartialData];
        expectedResult = service.addFailedAttemptToCollectionIfMissing(failedAttemptCollection, failedAttempt);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(failedAttempt);
      });

      it('should add only unique FailedAttempt to an array', () => {
        const failedAttemptArray: IFailedAttempt[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const failedAttemptCollection: IFailedAttempt[] = [sampleWithRequiredData];
        expectedResult = service.addFailedAttemptToCollectionIfMissing(failedAttemptCollection, ...failedAttemptArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const failedAttempt: IFailedAttempt = sampleWithRequiredData;
        const failedAttempt2: IFailedAttempt = sampleWithPartialData;
        expectedResult = service.addFailedAttemptToCollectionIfMissing([], failedAttempt, failedAttempt2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(failedAttempt);
        expect(expectedResult).toContain(failedAttempt2);
      });

      it('should accept null and undefined values', () => {
        const failedAttempt: IFailedAttempt = sampleWithRequiredData;
        expectedResult = service.addFailedAttemptToCollectionIfMissing([], null, failedAttempt, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(failedAttempt);
      });

      it('should return initial array if no FailedAttempt is added', () => {
        const failedAttemptCollection: IFailedAttempt[] = [sampleWithRequiredData];
        expectedResult = service.addFailedAttemptToCollectionIfMissing(failedAttemptCollection, undefined, null);
        expect(expectedResult).toEqual(failedAttemptCollection);
      });
    });

    describe('compareFailedAttempt', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFailedAttempt(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareFailedAttempt(entity1, entity2);
        const compareResult2 = service.compareFailedAttempt(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareFailedAttempt(entity1, entity2);
        const compareResult2 = service.compareFailedAttempt(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareFailedAttempt(entity1, entity2);
        const compareResult2 = service.compareFailedAttempt(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
