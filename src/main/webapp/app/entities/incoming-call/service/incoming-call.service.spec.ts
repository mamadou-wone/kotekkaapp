import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IIncomingCall } from '../incoming-call.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../incoming-call.test-samples';

import { IncomingCallService, RestIncomingCall } from './incoming-call.service';

const requireRestSample: RestIncomingCall = {
  ...sampleWithRequiredData,
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
  responseTime: sampleWithRequiredData.responseTime?.toJSON(),
};

describe('IncomingCall Service', () => {
  let service: IncomingCallService;
  let httpMock: HttpTestingController;
  let expectedResult: IIncomingCall | IIncomingCall[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(IncomingCallService);
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

    it('should create a IncomingCall', () => {
      const incomingCall = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(incomingCall).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a IncomingCall', () => {
      const incomingCall = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(incomingCall).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a IncomingCall', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of IncomingCall', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a IncomingCall', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addIncomingCallToCollectionIfMissing', () => {
      it('should add a IncomingCall to an empty array', () => {
        const incomingCall: IIncomingCall = sampleWithRequiredData;
        expectedResult = service.addIncomingCallToCollectionIfMissing([], incomingCall);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(incomingCall);
      });

      it('should not add a IncomingCall to an array that contains it', () => {
        const incomingCall: IIncomingCall = sampleWithRequiredData;
        const incomingCallCollection: IIncomingCall[] = [
          {
            ...incomingCall,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addIncomingCallToCollectionIfMissing(incomingCallCollection, incomingCall);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a IncomingCall to an array that doesn't contain it", () => {
        const incomingCall: IIncomingCall = sampleWithRequiredData;
        const incomingCallCollection: IIncomingCall[] = [sampleWithPartialData];
        expectedResult = service.addIncomingCallToCollectionIfMissing(incomingCallCollection, incomingCall);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(incomingCall);
      });

      it('should add only unique IncomingCall to an array', () => {
        const incomingCallArray: IIncomingCall[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const incomingCallCollection: IIncomingCall[] = [sampleWithRequiredData];
        expectedResult = service.addIncomingCallToCollectionIfMissing(incomingCallCollection, ...incomingCallArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const incomingCall: IIncomingCall = sampleWithRequiredData;
        const incomingCall2: IIncomingCall = sampleWithPartialData;
        expectedResult = service.addIncomingCallToCollectionIfMissing([], incomingCall, incomingCall2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(incomingCall);
        expect(expectedResult).toContain(incomingCall2);
      });

      it('should accept null and undefined values', () => {
        const incomingCall: IIncomingCall = sampleWithRequiredData;
        expectedResult = service.addIncomingCallToCollectionIfMissing([], null, incomingCall, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(incomingCall);
      });

      it('should return initial array if no IncomingCall is added', () => {
        const incomingCallCollection: IIncomingCall[] = [sampleWithRequiredData];
        expectedResult = service.addIncomingCallToCollectionIfMissing(incomingCallCollection, undefined, null);
        expect(expectedResult).toEqual(incomingCallCollection);
      });
    });

    describe('compareIncomingCall', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareIncomingCall(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareIncomingCall(entity1, entity2);
        const compareResult2 = service.compareIncomingCall(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareIncomingCall(entity1, entity2);
        const compareResult2 = service.compareIncomingCall(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareIncomingCall(entity1, entity2);
        const compareResult2 = service.compareIncomingCall(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
