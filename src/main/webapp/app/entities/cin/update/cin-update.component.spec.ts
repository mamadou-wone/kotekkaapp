import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { CinService } from '../service/cin.service';
import { ICin } from '../cin.model';
import { CinFormService } from './cin-form.service';

import { CinUpdateComponent } from './cin-update.component';

describe('Cin Management Update Component', () => {
  let comp: CinUpdateComponent;
  let fixture: ComponentFixture<CinUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let cinFormService: CinFormService;
  let cinService: CinService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CinUpdateComponent],
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
      .overrideTemplate(CinUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CinUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    cinFormService = TestBed.inject(CinFormService);
    cinService = TestBed.inject(CinService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const cin: ICin = { id: 456 };

      activatedRoute.data = of({ cin });
      comp.ngOnInit();

      expect(comp.cin).toEqual(cin);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICin>>();
      const cin = { id: 123 };
      jest.spyOn(cinFormService, 'getCin').mockReturnValue(cin);
      jest.spyOn(cinService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cin });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cin }));
      saveSubject.complete();

      // THEN
      expect(cinFormService.getCin).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(cinService.update).toHaveBeenCalledWith(expect.objectContaining(cin));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICin>>();
      const cin = { id: 123 };
      jest.spyOn(cinFormService, 'getCin').mockReturnValue({ id: null });
      jest.spyOn(cinService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cin: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cin }));
      saveSubject.complete();

      // THEN
      expect(cinFormService.getCin).toHaveBeenCalled();
      expect(cinService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICin>>();
      const cin = { id: 123 };
      jest.spyOn(cinService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cin });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(cinService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
