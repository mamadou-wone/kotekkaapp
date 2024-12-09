import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { FailedAttemptHistoryService } from '../service/failed-attempt-history.service';
import { IFailedAttemptHistory } from '../failed-attempt-history.model';
import { FailedAttemptHistoryFormService } from './failed-attempt-history-form.service';

import { FailedAttemptHistoryUpdateComponent } from './failed-attempt-history-update.component';

describe('FailedAttemptHistory Management Update Component', () => {
  let comp: FailedAttemptHistoryUpdateComponent;
  let fixture: ComponentFixture<FailedAttemptHistoryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let failedAttemptHistoryFormService: FailedAttemptHistoryFormService;
  let failedAttemptHistoryService: FailedAttemptHistoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [FailedAttemptHistoryUpdateComponent],
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
      .overrideTemplate(FailedAttemptHistoryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FailedAttemptHistoryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    failedAttemptHistoryFormService = TestBed.inject(FailedAttemptHistoryFormService);
    failedAttemptHistoryService = TestBed.inject(FailedAttemptHistoryService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const failedAttemptHistory: IFailedAttemptHistory = { id: 456 };

      activatedRoute.data = of({ failedAttemptHistory });
      comp.ngOnInit();

      expect(comp.failedAttemptHistory).toEqual(failedAttemptHistory);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFailedAttemptHistory>>();
      const failedAttemptHistory = { id: 123 };
      jest.spyOn(failedAttemptHistoryFormService, 'getFailedAttemptHistory').mockReturnValue(failedAttemptHistory);
      jest.spyOn(failedAttemptHistoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ failedAttemptHistory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: failedAttemptHistory }));
      saveSubject.complete();

      // THEN
      expect(failedAttemptHistoryFormService.getFailedAttemptHistory).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(failedAttemptHistoryService.update).toHaveBeenCalledWith(expect.objectContaining(failedAttemptHistory));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFailedAttemptHistory>>();
      const failedAttemptHistory = { id: 123 };
      jest.spyOn(failedAttemptHistoryFormService, 'getFailedAttemptHistory').mockReturnValue({ id: null });
      jest.spyOn(failedAttemptHistoryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ failedAttemptHistory: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: failedAttemptHistory }));
      saveSubject.complete();

      // THEN
      expect(failedAttemptHistoryFormService.getFailedAttemptHistory).toHaveBeenCalled();
      expect(failedAttemptHistoryService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFailedAttemptHistory>>();
      const failedAttemptHistory = { id: 123 };
      jest.spyOn(failedAttemptHistoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ failedAttemptHistory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(failedAttemptHistoryService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
