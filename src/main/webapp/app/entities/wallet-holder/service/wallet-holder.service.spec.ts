import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IWalletHolder } from '../wallet-holder.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../wallet-holder.test-samples';

import { RestWalletHolder, WalletHolderService } from './wallet-holder.service';

const requireRestSample: RestWalletHolder = {
  ...sampleWithRequiredData,
  dateOfBirth: sampleWithRequiredData.dateOfBirth?.format(DATE_FORMAT),
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
  lastModifiedDate: sampleWithRequiredData.lastModifiedDate?.toJSON(),
};

describe('WalletHolder Service', () => {
  let service: WalletHolderService;
  let httpMock: HttpTestingController;
  let expectedResult: IWalletHolder | IWalletHolder[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(WalletHolderService);
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

    it('should create a WalletHolder', () => {
      const walletHolder = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(walletHolder).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a WalletHolder', () => {
      const walletHolder = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(walletHolder).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a WalletHolder', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of WalletHolder', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a WalletHolder', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addWalletHolderToCollectionIfMissing', () => {
      it('should add a WalletHolder to an empty array', () => {
        const walletHolder: IWalletHolder = sampleWithRequiredData;
        expectedResult = service.addWalletHolderToCollectionIfMissing([], walletHolder);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(walletHolder);
      });

      it('should not add a WalletHolder to an array that contains it', () => {
        const walletHolder: IWalletHolder = sampleWithRequiredData;
        const walletHolderCollection: IWalletHolder[] = [
          {
            ...walletHolder,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addWalletHolderToCollectionIfMissing(walletHolderCollection, walletHolder);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a WalletHolder to an array that doesn't contain it", () => {
        const walletHolder: IWalletHolder = sampleWithRequiredData;
        const walletHolderCollection: IWalletHolder[] = [sampleWithPartialData];
        expectedResult = service.addWalletHolderToCollectionIfMissing(walletHolderCollection, walletHolder);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(walletHolder);
      });

      it('should add only unique WalletHolder to an array', () => {
        const walletHolderArray: IWalletHolder[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const walletHolderCollection: IWalletHolder[] = [sampleWithRequiredData];
        expectedResult = service.addWalletHolderToCollectionIfMissing(walletHolderCollection, ...walletHolderArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const walletHolder: IWalletHolder = sampleWithRequiredData;
        const walletHolder2: IWalletHolder = sampleWithPartialData;
        expectedResult = service.addWalletHolderToCollectionIfMissing([], walletHolder, walletHolder2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(walletHolder);
        expect(expectedResult).toContain(walletHolder2);
      });

      it('should accept null and undefined values', () => {
        const walletHolder: IWalletHolder = sampleWithRequiredData;
        expectedResult = service.addWalletHolderToCollectionIfMissing([], null, walletHolder, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(walletHolder);
      });

      it('should return initial array if no WalletHolder is added', () => {
        const walletHolderCollection: IWalletHolder[] = [sampleWithRequiredData];
        expectedResult = service.addWalletHolderToCollectionIfMissing(walletHolderCollection, undefined, null);
        expect(expectedResult).toEqual(walletHolderCollection);
      });
    });

    describe('compareWalletHolder', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareWalletHolder(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareWalletHolder(entity1, entity2);
        const compareResult2 = service.compareWalletHolder(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareWalletHolder(entity1, entity2);
        const compareResult2 = service.compareWalletHolder(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareWalletHolder(entity1, entity2);
        const compareResult2 = service.compareWalletHolder(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
