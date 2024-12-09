import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IUserPreference } from '../user-preference.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../user-preference.test-samples';

import { RestUserPreference, UserPreferenceService } from './user-preference.service';

const requireRestSample: RestUserPreference = {
  ...sampleWithRequiredData,
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
};

describe('UserPreference Service', () => {
  let service: UserPreferenceService;
  let httpMock: HttpTestingController;
  let expectedResult: IUserPreference | IUserPreference[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(UserPreferenceService);
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

    it('should create a UserPreference', () => {
      const userPreference = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(userPreference).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a UserPreference', () => {
      const userPreference = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(userPreference).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a UserPreference', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of UserPreference', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a UserPreference', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addUserPreferenceToCollectionIfMissing', () => {
      it('should add a UserPreference to an empty array', () => {
        const userPreference: IUserPreference = sampleWithRequiredData;
        expectedResult = service.addUserPreferenceToCollectionIfMissing([], userPreference);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(userPreference);
      });

      it('should not add a UserPreference to an array that contains it', () => {
        const userPreference: IUserPreference = sampleWithRequiredData;
        const userPreferenceCollection: IUserPreference[] = [
          {
            ...userPreference,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addUserPreferenceToCollectionIfMissing(userPreferenceCollection, userPreference);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a UserPreference to an array that doesn't contain it", () => {
        const userPreference: IUserPreference = sampleWithRequiredData;
        const userPreferenceCollection: IUserPreference[] = [sampleWithPartialData];
        expectedResult = service.addUserPreferenceToCollectionIfMissing(userPreferenceCollection, userPreference);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(userPreference);
      });

      it('should add only unique UserPreference to an array', () => {
        const userPreferenceArray: IUserPreference[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const userPreferenceCollection: IUserPreference[] = [sampleWithRequiredData];
        expectedResult = service.addUserPreferenceToCollectionIfMissing(userPreferenceCollection, ...userPreferenceArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const userPreference: IUserPreference = sampleWithRequiredData;
        const userPreference2: IUserPreference = sampleWithPartialData;
        expectedResult = service.addUserPreferenceToCollectionIfMissing([], userPreference, userPreference2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(userPreference);
        expect(expectedResult).toContain(userPreference2);
      });

      it('should accept null and undefined values', () => {
        const userPreference: IUserPreference = sampleWithRequiredData;
        expectedResult = service.addUserPreferenceToCollectionIfMissing([], null, userPreference, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(userPreference);
      });

      it('should return initial array if no UserPreference is added', () => {
        const userPreferenceCollection: IUserPreference[] = [sampleWithRequiredData];
        expectedResult = service.addUserPreferenceToCollectionIfMissing(userPreferenceCollection, undefined, null);
        expect(expectedResult).toEqual(userPreferenceCollection);
      });
    });

    describe('compareUserPreference', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareUserPreference(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareUserPreference(entity1, entity2);
        const compareResult2 = service.compareUserPreference(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareUserPreference(entity1, entity2);
        const compareResult2 = service.compareUserPreference(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareUserPreference(entity1, entity2);
        const compareResult2 = service.compareUserPreference(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
