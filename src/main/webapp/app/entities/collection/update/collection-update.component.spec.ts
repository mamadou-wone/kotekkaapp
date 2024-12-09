import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { CollectionService } from '../service/collection.service';
import { ICollection } from '../collection.model';
import { CollectionFormService } from './collection-form.service';

import { CollectionUpdateComponent } from './collection-update.component';

describe('Collection Management Update Component', () => {
  let comp: CollectionUpdateComponent;
  let fixture: ComponentFixture<CollectionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let collectionFormService: CollectionFormService;
  let collectionService: CollectionService;
  let productService: ProductService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CollectionUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(CollectionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CollectionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    collectionFormService = TestBed.inject(CollectionFormService);
    collectionService = TestBed.inject(CollectionService);
    productService = TestBed.inject(ProductService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Product query and add missing value', () => {
      const collection: ICollection = { id: 456 };
      const products: IProduct[] = [{ id: 14175 }];
      collection.products = products;

      const productCollection: IProduct[] = [{ id: 21776 }];
      jest.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
      const additionalProducts = [...products];
      const expectedCollection: IProduct[] = [...additionalProducts, ...productCollection];
      jest.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ collection });
      comp.ngOnInit();

      expect(productService.query).toHaveBeenCalled();
      expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(
        productCollection,
        ...additionalProducts.map(expect.objectContaining),
      );
      expect(comp.productsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const collection: ICollection = { id: 456 };
      const product: IProduct = { id: 14821 };
      collection.products = [product];

      activatedRoute.data = of({ collection });
      comp.ngOnInit();

      expect(comp.productsSharedCollection).toContain(product);
      expect(comp.collection).toEqual(collection);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICollection>>();
      const collection = { id: 123 };
      jest.spyOn(collectionFormService, 'getCollection').mockReturnValue(collection);
      jest.spyOn(collectionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ collection });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: collection }));
      saveSubject.complete();

      // THEN
      expect(collectionFormService.getCollection).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(collectionService.update).toHaveBeenCalledWith(expect.objectContaining(collection));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICollection>>();
      const collection = { id: 123 };
      jest.spyOn(collectionFormService, 'getCollection').mockReturnValue({ id: null });
      jest.spyOn(collectionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ collection: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: collection }));
      saveSubject.complete();

      // THEN
      expect(collectionFormService.getCollection).toHaveBeenCalled();
      expect(collectionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICollection>>();
      const collection = { id: 123 };
      jest.spyOn(collectionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ collection });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(collectionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareProduct', () => {
      it('Should forward to productService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(productService, 'compareProduct');
        comp.compareProduct(entity, entity2);
        expect(productService.compareProduct).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
