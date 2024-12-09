import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IPartnerCall } from '../partner-call.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../partner-call.test-samples';

import { PartnerCallService, RestPartnerCall } from './partner-call.service';

const requireRestSample: RestPartnerCall = {
  ...sampleWithRequiredData,
  requestTime: sampleWithRequiredData.requestTime?.toJSON(),
  responseTime: sampleWithRequiredData.responseTime?.toJSON(),
};

describe('PartnerCall Service', () => {
  let service: PartnerCallService;
  let httpMock: HttpTestingController;
  let expectedResult: IPartnerCall | IPartnerCall[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(PartnerCallService);
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

    it('should create a PartnerCall', () => {
      const partnerCall = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(partnerCall).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PartnerCall', () => {
      const partnerCall = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(partnerCall).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PartnerCall', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PartnerCall', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PartnerCall', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPartnerCallToCollectionIfMissing', () => {
      it('should add a PartnerCall to an empty array', () => {
        const partnerCall: IPartnerCall = sampleWithRequiredData;
        expectedResult = service.addPartnerCallToCollectionIfMissing([], partnerCall);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(partnerCall);
      });

      it('should not add a PartnerCall to an array that contains it', () => {
        const partnerCall: IPartnerCall = sampleWithRequiredData;
        const partnerCallCollection: IPartnerCall[] = [
          {
            ...partnerCall,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPartnerCallToCollectionIfMissing(partnerCallCollection, partnerCall);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PartnerCall to an array that doesn't contain it", () => {
        const partnerCall: IPartnerCall = sampleWithRequiredData;
        const partnerCallCollection: IPartnerCall[] = [sampleWithPartialData];
        expectedResult = service.addPartnerCallToCollectionIfMissing(partnerCallCollection, partnerCall);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(partnerCall);
      });

      it('should add only unique PartnerCall to an array', () => {
        const partnerCallArray: IPartnerCall[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const partnerCallCollection: IPartnerCall[] = [sampleWithRequiredData];
        expectedResult = service.addPartnerCallToCollectionIfMissing(partnerCallCollection, ...partnerCallArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const partnerCall: IPartnerCall = sampleWithRequiredData;
        const partnerCall2: IPartnerCall = sampleWithPartialData;
        expectedResult = service.addPartnerCallToCollectionIfMissing([], partnerCall, partnerCall2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(partnerCall);
        expect(expectedResult).toContain(partnerCall2);
      });

      it('should accept null and undefined values', () => {
        const partnerCall: IPartnerCall = sampleWithRequiredData;
        expectedResult = service.addPartnerCallToCollectionIfMissing([], null, partnerCall, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(partnerCall);
      });

      it('should return initial array if no PartnerCall is added', () => {
        const partnerCallCollection: IPartnerCall[] = [sampleWithRequiredData];
        expectedResult = service.addPartnerCallToCollectionIfMissing(partnerCallCollection, undefined, null);
        expect(expectedResult).toEqual(partnerCallCollection);
      });
    });

    describe('comparePartnerCall', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePartnerCall(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.comparePartnerCall(entity1, entity2);
        const compareResult2 = service.comparePartnerCall(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.comparePartnerCall(entity1, entity2);
        const compareResult2 = service.comparePartnerCall(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.comparePartnerCall(entity1, entity2);
        const compareResult2 = service.comparePartnerCall(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
