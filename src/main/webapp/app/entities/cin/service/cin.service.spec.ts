import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ICin } from '../cin.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../cin.test-samples';

import { CinService, RestCin } from './cin.service';

const requireRestSample: RestCin = {
  ...sampleWithRequiredData,
  validityDate: sampleWithRequiredData.validityDate?.format(DATE_FORMAT),
  birthDate: sampleWithRequiredData.birthDate?.format(DATE_FORMAT),
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
};

describe('Cin Service', () => {
  let service: CinService;
  let httpMock: HttpTestingController;
  let expectedResult: ICin | ICin[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(CinService);
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

    it('should create a Cin', () => {
      const cin = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(cin).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Cin', () => {
      const cin = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(cin).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Cin', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Cin', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Cin', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCinToCollectionIfMissing', () => {
      it('should add a Cin to an empty array', () => {
        const cin: ICin = sampleWithRequiredData;
        expectedResult = service.addCinToCollectionIfMissing([], cin);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cin);
      });

      it('should not add a Cin to an array that contains it', () => {
        const cin: ICin = sampleWithRequiredData;
        const cinCollection: ICin[] = [
          {
            ...cin,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCinToCollectionIfMissing(cinCollection, cin);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Cin to an array that doesn't contain it", () => {
        const cin: ICin = sampleWithRequiredData;
        const cinCollection: ICin[] = [sampleWithPartialData];
        expectedResult = service.addCinToCollectionIfMissing(cinCollection, cin);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cin);
      });

      it('should add only unique Cin to an array', () => {
        const cinArray: ICin[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const cinCollection: ICin[] = [sampleWithRequiredData];
        expectedResult = service.addCinToCollectionIfMissing(cinCollection, ...cinArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const cin: ICin = sampleWithRequiredData;
        const cin2: ICin = sampleWithPartialData;
        expectedResult = service.addCinToCollectionIfMissing([], cin, cin2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cin);
        expect(expectedResult).toContain(cin2);
      });

      it('should accept null and undefined values', () => {
        const cin: ICin = sampleWithRequiredData;
        expectedResult = service.addCinToCollectionIfMissing([], null, cin, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cin);
      });

      it('should return initial array if no Cin is added', () => {
        const cinCollection: ICin[] = [sampleWithRequiredData];
        expectedResult = service.addCinToCollectionIfMissing(cinCollection, undefined, null);
        expect(expectedResult).toEqual(cinCollection);
      });
    });

    describe('compareCin', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCin(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareCin(entity1, entity2);
        const compareResult2 = service.compareCin(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareCin(entity1, entity2);
        const compareResult2 = service.compareCin(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareCin(entity1, entity2);
        const compareResult2 = service.compareCin(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
