import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IUserAffiliation } from '../user-affiliation.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../user-affiliation.test-samples';

import { RestUserAffiliation, UserAffiliationService } from './user-affiliation.service';

const requireRestSample: RestUserAffiliation = {
  ...sampleWithRequiredData,
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
};

describe('UserAffiliation Service', () => {
  let service: UserAffiliationService;
  let httpMock: HttpTestingController;
  let expectedResult: IUserAffiliation | IUserAffiliation[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(UserAffiliationService);
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

    it('should create a UserAffiliation', () => {
      const userAffiliation = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(userAffiliation).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a UserAffiliation', () => {
      const userAffiliation = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(userAffiliation).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a UserAffiliation', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of UserAffiliation', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a UserAffiliation', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addUserAffiliationToCollectionIfMissing', () => {
      it('should add a UserAffiliation to an empty array', () => {
        const userAffiliation: IUserAffiliation = sampleWithRequiredData;
        expectedResult = service.addUserAffiliationToCollectionIfMissing([], userAffiliation);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(userAffiliation);
      });

      it('should not add a UserAffiliation to an array that contains it', () => {
        const userAffiliation: IUserAffiliation = sampleWithRequiredData;
        const userAffiliationCollection: IUserAffiliation[] = [
          {
            ...userAffiliation,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addUserAffiliationToCollectionIfMissing(userAffiliationCollection, userAffiliation);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a UserAffiliation to an array that doesn't contain it", () => {
        const userAffiliation: IUserAffiliation = sampleWithRequiredData;
        const userAffiliationCollection: IUserAffiliation[] = [sampleWithPartialData];
        expectedResult = service.addUserAffiliationToCollectionIfMissing(userAffiliationCollection, userAffiliation);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(userAffiliation);
      });

      it('should add only unique UserAffiliation to an array', () => {
        const userAffiliationArray: IUserAffiliation[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const userAffiliationCollection: IUserAffiliation[] = [sampleWithRequiredData];
        expectedResult = service.addUserAffiliationToCollectionIfMissing(userAffiliationCollection, ...userAffiliationArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const userAffiliation: IUserAffiliation = sampleWithRequiredData;
        const userAffiliation2: IUserAffiliation = sampleWithPartialData;
        expectedResult = service.addUserAffiliationToCollectionIfMissing([], userAffiliation, userAffiliation2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(userAffiliation);
        expect(expectedResult).toContain(userAffiliation2);
      });

      it('should accept null and undefined values', () => {
        const userAffiliation: IUserAffiliation = sampleWithRequiredData;
        expectedResult = service.addUserAffiliationToCollectionIfMissing([], null, userAffiliation, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(userAffiliation);
      });

      it('should return initial array if no UserAffiliation is added', () => {
        const userAffiliationCollection: IUserAffiliation[] = [sampleWithRequiredData];
        expectedResult = service.addUserAffiliationToCollectionIfMissing(userAffiliationCollection, undefined, null);
        expect(expectedResult).toEqual(userAffiliationCollection);
      });
    });

    describe('compareUserAffiliation', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareUserAffiliation(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareUserAffiliation(entity1, entity2);
        const compareResult2 = service.compareUserAffiliation(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareUserAffiliation(entity1, entity2);
        const compareResult2 = service.compareUserAffiliation(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareUserAffiliation(entity1, entity2);
        const compareResult2 = service.compareUserAffiliation(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
