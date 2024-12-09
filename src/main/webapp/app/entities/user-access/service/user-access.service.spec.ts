import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IUserAccess } from '../user-access.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../user-access.test-samples';

import { RestUserAccess, UserAccessService } from './user-access.service';

const requireRestSample: RestUserAccess = {
  ...sampleWithRequiredData,
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
};

describe('UserAccess Service', () => {
  let service: UserAccessService;
  let httpMock: HttpTestingController;
  let expectedResult: IUserAccess | IUserAccess[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(UserAccessService);
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

    it('should create a UserAccess', () => {
      const userAccess = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(userAccess).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a UserAccess', () => {
      const userAccess = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(userAccess).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a UserAccess', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of UserAccess', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a UserAccess', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addUserAccessToCollectionIfMissing', () => {
      it('should add a UserAccess to an empty array', () => {
        const userAccess: IUserAccess = sampleWithRequiredData;
        expectedResult = service.addUserAccessToCollectionIfMissing([], userAccess);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(userAccess);
      });

      it('should not add a UserAccess to an array that contains it', () => {
        const userAccess: IUserAccess = sampleWithRequiredData;
        const userAccessCollection: IUserAccess[] = [
          {
            ...userAccess,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addUserAccessToCollectionIfMissing(userAccessCollection, userAccess);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a UserAccess to an array that doesn't contain it", () => {
        const userAccess: IUserAccess = sampleWithRequiredData;
        const userAccessCollection: IUserAccess[] = [sampleWithPartialData];
        expectedResult = service.addUserAccessToCollectionIfMissing(userAccessCollection, userAccess);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(userAccess);
      });

      it('should add only unique UserAccess to an array', () => {
        const userAccessArray: IUserAccess[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const userAccessCollection: IUserAccess[] = [sampleWithRequiredData];
        expectedResult = service.addUserAccessToCollectionIfMissing(userAccessCollection, ...userAccessArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const userAccess: IUserAccess = sampleWithRequiredData;
        const userAccess2: IUserAccess = sampleWithPartialData;
        expectedResult = service.addUserAccessToCollectionIfMissing([], userAccess, userAccess2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(userAccess);
        expect(expectedResult).toContain(userAccess2);
      });

      it('should accept null and undefined values', () => {
        const userAccess: IUserAccess = sampleWithRequiredData;
        expectedResult = service.addUserAccessToCollectionIfMissing([], null, userAccess, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(userAccess);
      });

      it('should return initial array if no UserAccess is added', () => {
        const userAccessCollection: IUserAccess[] = [sampleWithRequiredData];
        expectedResult = service.addUserAccessToCollectionIfMissing(userAccessCollection, undefined, null);
        expect(expectedResult).toEqual(userAccessCollection);
      });
    });

    describe('compareUserAccess', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareUserAccess(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareUserAccess(entity1, entity2);
        const compareResult2 = service.compareUserAccess(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareUserAccess(entity1, entity2);
        const compareResult2 = service.compareUserAccess(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareUserAccess(entity1, entity2);
        const compareResult2 = service.compareUserAccess(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
