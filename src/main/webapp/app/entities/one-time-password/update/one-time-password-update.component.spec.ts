import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { OneTimePasswordService } from '../service/one-time-password.service';
import { IOneTimePassword } from '../one-time-password.model';
import { OneTimePasswordFormService } from './one-time-password-form.service';

import { OneTimePasswordUpdateComponent } from './one-time-password-update.component';

describe('OneTimePassword Management Update Component', () => {
  let comp: OneTimePasswordUpdateComponent;
  let fixture: ComponentFixture<OneTimePasswordUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let oneTimePasswordFormService: OneTimePasswordFormService;
  let oneTimePasswordService: OneTimePasswordService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [OneTimePasswordUpdateComponent],
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
      .overrideTemplate(OneTimePasswordUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OneTimePasswordUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    oneTimePasswordFormService = TestBed.inject(OneTimePasswordFormService);
    oneTimePasswordService = TestBed.inject(OneTimePasswordService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const oneTimePassword: IOneTimePassword = { id: 456 };

      activatedRoute.data = of({ oneTimePassword });
      comp.ngOnInit();

      expect(comp.oneTimePassword).toEqual(oneTimePassword);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOneTimePassword>>();
      const oneTimePassword = { id: 123 };
      jest.spyOn(oneTimePasswordFormService, 'getOneTimePassword').mockReturnValue(oneTimePassword);
      jest.spyOn(oneTimePasswordService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ oneTimePassword });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: oneTimePassword }));
      saveSubject.complete();

      // THEN
      expect(oneTimePasswordFormService.getOneTimePassword).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(oneTimePasswordService.update).toHaveBeenCalledWith(expect.objectContaining(oneTimePassword));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOneTimePassword>>();
      const oneTimePassword = { id: 123 };
      jest.spyOn(oneTimePasswordFormService, 'getOneTimePassword').mockReturnValue({ id: null });
      jest.spyOn(oneTimePasswordService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ oneTimePassword: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: oneTimePassword }));
      saveSubject.complete();

      // THEN
      expect(oneTimePasswordFormService.getOneTimePassword).toHaveBeenCalled();
      expect(oneTimePasswordService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOneTimePassword>>();
      const oneTimePassword = { id: 123 };
      jest.spyOn(oneTimePasswordService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ oneTimePassword });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(oneTimePasswordService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
