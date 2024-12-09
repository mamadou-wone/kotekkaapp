import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IOneTimePassword } from '../one-time-password.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../one-time-password.test-samples';

import { OneTimePasswordService, RestOneTimePassword } from './one-time-password.service';

const requireRestSample: RestOneTimePassword = {
  ...sampleWithRequiredData,
  expiry: sampleWithRequiredData.expiry?.toJSON(),
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
};

describe('OneTimePassword Service', () => {
  let service: OneTimePasswordService;
  let httpMock: HttpTestingController;
  let expectedResult: IOneTimePassword | IOneTimePassword[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(OneTimePasswordService);
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

    it('should create a OneTimePassword', () => {
      const oneTimePassword = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(oneTimePassword).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a OneTimePassword', () => {
      const oneTimePassword = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(oneTimePassword).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a OneTimePassword', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of OneTimePassword', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a OneTimePassword', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addOneTimePasswordToCollectionIfMissing', () => {
      it('should add a OneTimePassword to an empty array', () => {
        const oneTimePassword: IOneTimePassword = sampleWithRequiredData;
        expectedResult = service.addOneTimePasswordToCollectionIfMissing([], oneTimePassword);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(oneTimePassword);
      });

      it('should not add a OneTimePassword to an array that contains it', () => {
        const oneTimePassword: IOneTimePassword = sampleWithRequiredData;
        const oneTimePasswordCollection: IOneTimePassword[] = [
          {
            ...oneTimePassword,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addOneTimePasswordToCollectionIfMissing(oneTimePasswordCollection, oneTimePassword);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a OneTimePassword to an array that doesn't contain it", () => {
        const oneTimePassword: IOneTimePassword = sampleWithRequiredData;
        const oneTimePasswordCollection: IOneTimePassword[] = [sampleWithPartialData];
        expectedResult = service.addOneTimePasswordToCollectionIfMissing(oneTimePasswordCollection, oneTimePassword);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(oneTimePassword);
      });

      it('should add only unique OneTimePassword to an array', () => {
        const oneTimePasswordArray: IOneTimePassword[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const oneTimePasswordCollection: IOneTimePassword[] = [sampleWithRequiredData];
        expectedResult = service.addOneTimePasswordToCollectionIfMissing(oneTimePasswordCollection, ...oneTimePasswordArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const oneTimePassword: IOneTimePassword = sampleWithRequiredData;
        const oneTimePassword2: IOneTimePassword = sampleWithPartialData;
        expectedResult = service.addOneTimePasswordToCollectionIfMissing([], oneTimePassword, oneTimePassword2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(oneTimePassword);
        expect(expectedResult).toContain(oneTimePassword2);
      });

      it('should accept null and undefined values', () => {
        const oneTimePassword: IOneTimePassword = sampleWithRequiredData;
        expectedResult = service.addOneTimePasswordToCollectionIfMissing([], null, oneTimePassword, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(oneTimePassword);
      });

      it('should return initial array if no OneTimePassword is added', () => {
        const oneTimePasswordCollection: IOneTimePassword[] = [sampleWithRequiredData];
        expectedResult = service.addOneTimePasswordToCollectionIfMissing(oneTimePasswordCollection, undefined, null);
        expect(expectedResult).toEqual(oneTimePasswordCollection);
      });
    });

    describe('compareOneTimePassword', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareOneTimePassword(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareOneTimePassword(entity1, entity2);
        const compareResult2 = service.compareOneTimePassword(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareOneTimePassword(entity1, entity2);
        const compareResult2 = service.compareOneTimePassword(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareOneTimePassword(entity1, entity2);
        const compareResult2 = service.compareOneTimePassword(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
