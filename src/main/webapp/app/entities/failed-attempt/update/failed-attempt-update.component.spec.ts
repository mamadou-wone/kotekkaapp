import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { FailedAttemptService } from '../service/failed-attempt.service';
import { IFailedAttempt } from '../failed-attempt.model';
import { FailedAttemptFormService } from './failed-attempt-form.service';

import { FailedAttemptUpdateComponent } from './failed-attempt-update.component';

describe('FailedAttempt Management Update Component', () => {
  let comp: FailedAttemptUpdateComponent;
  let fixture: ComponentFixture<FailedAttemptUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let failedAttemptFormService: FailedAttemptFormService;
  let failedAttemptService: FailedAttemptService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [FailedAttemptUpdateComponent],
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
      .overrideTemplate(FailedAttemptUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FailedAttemptUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    failedAttemptFormService = TestBed.inject(FailedAttemptFormService);
    failedAttemptService = TestBed.inject(FailedAttemptService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const failedAttempt: IFailedAttempt = { id: 456 };

      activatedRoute.data = of({ failedAttempt });
      comp.ngOnInit();

      expect(comp.failedAttempt).toEqual(failedAttempt);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFailedAttempt>>();
      const failedAttempt = { id: 123 };
      jest.spyOn(failedAttemptFormService, 'getFailedAttempt').mockReturnValue(failedAttempt);
      jest.spyOn(failedAttemptService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ failedAttempt });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: failedAttempt }));
      saveSubject.complete();

      // THEN
      expect(failedAttemptFormService.getFailedAttempt).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(failedAttemptService.update).toHaveBeenCalledWith(expect.objectContaining(failedAttempt));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFailedAttempt>>();
      const failedAttempt = { id: 123 };
      jest.spyOn(failedAttemptFormService, 'getFailedAttempt').mockReturnValue({ id: null });
      jest.spyOn(failedAttemptService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ failedAttempt: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: failedAttempt }));
      saveSubject.complete();

      // THEN
      expect(failedAttemptFormService.getFailedAttempt).toHaveBeenCalled();
      expect(failedAttemptService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFailedAttempt>>();
      const failedAttempt = { id: 123 };
      jest.spyOn(failedAttemptService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ failedAttempt });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(failedAttemptService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
