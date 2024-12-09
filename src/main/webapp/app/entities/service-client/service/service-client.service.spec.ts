import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IServiceClient } from '../service-client.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../service-client.test-samples';

import { RestServiceClient, ServiceClientService } from './service-client.service';

const requireRestSample: RestServiceClient = {
  ...sampleWithRequiredData,
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
  lastModifiedDate: sampleWithRequiredData.lastModifiedDate?.toJSON(),
};

describe('ServiceClient Service', () => {
  let service: ServiceClientService;
  let httpMock: HttpTestingController;
  let expectedResult: IServiceClient | IServiceClient[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ServiceClientService);
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

    it('should create a ServiceClient', () => {
      const serviceClient = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(serviceClient).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ServiceClient', () => {
      const serviceClient = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(serviceClient).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ServiceClient', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ServiceClient', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ServiceClient', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addServiceClientToCollectionIfMissing', () => {
      it('should add a ServiceClient to an empty array', () => {
        const serviceClient: IServiceClient = sampleWithRequiredData;
        expectedResult = service.addServiceClientToCollectionIfMissing([], serviceClient);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(serviceClient);
      });

      it('should not add a ServiceClient to an array that contains it', () => {
        const serviceClient: IServiceClient = sampleWithRequiredData;
        const serviceClientCollection: IServiceClient[] = [
          {
            ...serviceClient,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addServiceClientToCollectionIfMissing(serviceClientCollection, serviceClient);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ServiceClient to an array that doesn't contain it", () => {
        const serviceClient: IServiceClient = sampleWithRequiredData;
        const serviceClientCollection: IServiceClient[] = [sampleWithPartialData];
        expectedResult = service.addServiceClientToCollectionIfMissing(serviceClientCollection, serviceClient);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(serviceClient);
      });

      it('should add only unique ServiceClient to an array', () => {
        const serviceClientArray: IServiceClient[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const serviceClientCollection: IServiceClient[] = [sampleWithRequiredData];
        expectedResult = service.addServiceClientToCollectionIfMissing(serviceClientCollection, ...serviceClientArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const serviceClient: IServiceClient = sampleWithRequiredData;
        const serviceClient2: IServiceClient = sampleWithPartialData;
        expectedResult = service.addServiceClientToCollectionIfMissing([], serviceClient, serviceClient2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(serviceClient);
        expect(expectedResult).toContain(serviceClient2);
      });

      it('should accept null and undefined values', () => {
        const serviceClient: IServiceClient = sampleWithRequiredData;
        expectedResult = service.addServiceClientToCollectionIfMissing([], null, serviceClient, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(serviceClient);
      });

      it('should return initial array if no ServiceClient is added', () => {
        const serviceClientCollection: IServiceClient[] = [sampleWithRequiredData];
        expectedResult = service.addServiceClientToCollectionIfMissing(serviceClientCollection, undefined, null);
        expect(expectedResult).toEqual(serviceClientCollection);
      });
    });

    describe('compareServiceClient', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareServiceClient(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareServiceClient(entity1, entity2);
        const compareResult2 = service.compareServiceClient(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareServiceClient(entity1, entity2);
        const compareResult2 = service.compareServiceClient(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareServiceClient(entity1, entity2);
        const compareResult2 = service.compareServiceClient(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
