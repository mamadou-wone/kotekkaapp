import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IFeatureFlag } from '../feature-flag.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../feature-flag.test-samples';

import { FeatureFlagService, RestFeatureFlag } from './feature-flag.service';

const requireRestSample: RestFeatureFlag = {
  ...sampleWithRequiredData,
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
  updatedDate: sampleWithRequiredData.updatedDate?.toJSON(),
};

describe('FeatureFlag Service', () => {
  let service: FeatureFlagService;
  let httpMock: HttpTestingController;
  let expectedResult: IFeatureFlag | IFeatureFlag[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(FeatureFlagService);
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

    it('should create a FeatureFlag', () => {
      const featureFlag = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(featureFlag).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FeatureFlag', () => {
      const featureFlag = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(featureFlag).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FeatureFlag', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FeatureFlag', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a FeatureFlag', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addFeatureFlagToCollectionIfMissing', () => {
      it('should add a FeatureFlag to an empty array', () => {
        const featureFlag: IFeatureFlag = sampleWithRequiredData;
        expectedResult = service.addFeatureFlagToCollectionIfMissing([], featureFlag);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(featureFlag);
      });

      it('should not add a FeatureFlag to an array that contains it', () => {
        const featureFlag: IFeatureFlag = sampleWithRequiredData;
        const featureFlagCollection: IFeatureFlag[] = [
          {
            ...featureFlag,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFeatureFlagToCollectionIfMissing(featureFlagCollection, featureFlag);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FeatureFlag to an array that doesn't contain it", () => {
        const featureFlag: IFeatureFlag = sampleWithRequiredData;
        const featureFlagCollection: IFeatureFlag[] = [sampleWithPartialData];
        expectedResult = service.addFeatureFlagToCollectionIfMissing(featureFlagCollection, featureFlag);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(featureFlag);
      });

      it('should add only unique FeatureFlag to an array', () => {
        const featureFlagArray: IFeatureFlag[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const featureFlagCollection: IFeatureFlag[] = [sampleWithRequiredData];
        expectedResult = service.addFeatureFlagToCollectionIfMissing(featureFlagCollection, ...featureFlagArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const featureFlag: IFeatureFlag = sampleWithRequiredData;
        const featureFlag2: IFeatureFlag = sampleWithPartialData;
        expectedResult = service.addFeatureFlagToCollectionIfMissing([], featureFlag, featureFlag2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(featureFlag);
        expect(expectedResult).toContain(featureFlag2);
      });

      it('should accept null and undefined values', () => {
        const featureFlag: IFeatureFlag = sampleWithRequiredData;
        expectedResult = service.addFeatureFlagToCollectionIfMissing([], null, featureFlag, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(featureFlag);
      });

      it('should return initial array if no FeatureFlag is added', () => {
        const featureFlagCollection: IFeatureFlag[] = [sampleWithRequiredData];
        expectedResult = service.addFeatureFlagToCollectionIfMissing(featureFlagCollection, undefined, null);
        expect(expectedResult).toEqual(featureFlagCollection);
      });
    });

    describe('compareFeatureFlag', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFeatureFlag(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareFeatureFlag(entity1, entity2);
        const compareResult2 = service.compareFeatureFlag(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareFeatureFlag(entity1, entity2);
        const compareResult2 = service.compareFeatureFlag(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareFeatureFlag(entity1, entity2);
        const compareResult2 = service.compareFeatureFlag(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
