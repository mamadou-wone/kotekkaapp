import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { CacheInfoService } from '../service/cache-info.service';
import { ICacheInfo } from '../cache-info.model';
import { CacheInfoFormService } from './cache-info-form.service';

import { CacheInfoUpdateComponent } from './cache-info-update.component';

describe('CacheInfo Management Update Component', () => {
  let comp: CacheInfoUpdateComponent;
  let fixture: ComponentFixture<CacheInfoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let cacheInfoFormService: CacheInfoFormService;
  let cacheInfoService: CacheInfoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CacheInfoUpdateComponent],
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
      .overrideTemplate(CacheInfoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CacheInfoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    cacheInfoFormService = TestBed.inject(CacheInfoFormService);
    cacheInfoService = TestBed.inject(CacheInfoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const cacheInfo: ICacheInfo = { id: 456 };

      activatedRoute.data = of({ cacheInfo });
      comp.ngOnInit();

      expect(comp.cacheInfo).toEqual(cacheInfo);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICacheInfo>>();
      const cacheInfo = { id: 123 };
      jest.spyOn(cacheInfoFormService, 'getCacheInfo').mockReturnValue(cacheInfo);
      jest.spyOn(cacheInfoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cacheInfo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cacheInfo }));
      saveSubject.complete();

      // THEN
      expect(cacheInfoFormService.getCacheInfo).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(cacheInfoService.update).toHaveBeenCalledWith(expect.objectContaining(cacheInfo));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICacheInfo>>();
      const cacheInfo = { id: 123 };
      jest.spyOn(cacheInfoFormService, 'getCacheInfo').mockReturnValue({ id: null });
      jest.spyOn(cacheInfoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cacheInfo: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cacheInfo }));
      saveSubject.complete();

      // THEN
      expect(cacheInfoFormService.getCacheInfo).toHaveBeenCalled();
      expect(cacheInfoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICacheInfo>>();
      const cacheInfo = { id: 123 };
      jest.spyOn(cacheInfoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cacheInfo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(cacheInfoService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
