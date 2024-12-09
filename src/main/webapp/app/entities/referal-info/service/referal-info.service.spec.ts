import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IReferalInfo } from '../referal-info.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../referal-info.test-samples';

import { ReferalInfoService, RestReferalInfo } from './referal-info.service';

const requireRestSample: RestReferalInfo = {
  ...sampleWithRequiredData,
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
  lastModifiedDate: sampleWithRequiredData.lastModifiedDate?.toJSON(),
};

describe('ReferalInfo Service', () => {
  let service: ReferalInfoService;
  let httpMock: HttpTestingController;
  let expectedResult: IReferalInfo | IReferalInfo[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ReferalInfoService);
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

    it('should create a ReferalInfo', () => {
      const referalInfo = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(referalInfo).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ReferalInfo', () => {
      const referalInfo = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(referalInfo).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ReferalInfo', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ReferalInfo', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ReferalInfo', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addReferalInfoToCollectionIfMissing', () => {
      it('should add a ReferalInfo to an empty array', () => {
        const referalInfo: IReferalInfo = sampleWithRequiredData;
        expectedResult = service.addReferalInfoToCollectionIfMissing([], referalInfo);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(referalInfo);
      });

      it('should not add a ReferalInfo to an array that contains it', () => {
        const referalInfo: IReferalInfo = sampleWithRequiredData;
        const referalInfoCollection: IReferalInfo[] = [
          {
            ...referalInfo,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addReferalInfoToCollectionIfMissing(referalInfoCollection, referalInfo);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ReferalInfo to an array that doesn't contain it", () => {
        const referalInfo: IReferalInfo = sampleWithRequiredData;
        const referalInfoCollection: IReferalInfo[] = [sampleWithPartialData];
        expectedResult = service.addReferalInfoToCollectionIfMissing(referalInfoCollection, referalInfo);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(referalInfo);
      });

      it('should add only unique ReferalInfo to an array', () => {
        const referalInfoArray: IReferalInfo[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const referalInfoCollection: IReferalInfo[] = [sampleWithRequiredData];
        expectedResult = service.addReferalInfoToCollectionIfMissing(referalInfoCollection, ...referalInfoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const referalInfo: IReferalInfo = sampleWithRequiredData;
        const referalInfo2: IReferalInfo = sampleWithPartialData;
        expectedResult = service.addReferalInfoToCollectionIfMissing([], referalInfo, referalInfo2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(referalInfo);
        expect(expectedResult).toContain(referalInfo2);
      });

      it('should accept null and undefined values', () => {
        const referalInfo: IReferalInfo = sampleWithRequiredData;
        expectedResult = service.addReferalInfoToCollectionIfMissing([], null, referalInfo, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(referalInfo);
      });

      it('should return initial array if no ReferalInfo is added', () => {
        const referalInfoCollection: IReferalInfo[] = [sampleWithRequiredData];
        expectedResult = service.addReferalInfoToCollectionIfMissing(referalInfoCollection, undefined, null);
        expect(expectedResult).toEqual(referalInfoCollection);
      });
    });

    describe('compareReferalInfo', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareReferalInfo(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareReferalInfo(entity1, entity2);
        const compareResult2 = service.compareReferalInfo(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareReferalInfo(entity1, entity2);
        const compareResult2 = service.compareReferalInfo(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareReferalInfo(entity1, entity2);
        const compareResult2 = service.compareReferalInfo(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
