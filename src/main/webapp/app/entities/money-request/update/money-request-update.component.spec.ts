import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { MoneyRequestService } from '../service/money-request.service';
import { IMoneyRequest } from '../money-request.model';
import { MoneyRequestFormService } from './money-request-form.service';

import { MoneyRequestUpdateComponent } from './money-request-update.component';

describe('MoneyRequest Management Update Component', () => {
  let comp: MoneyRequestUpdateComponent;
  let fixture: ComponentFixture<MoneyRequestUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let moneyRequestFormService: MoneyRequestFormService;
  let moneyRequestService: MoneyRequestService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [MoneyRequestUpdateComponent],
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
      .overrideTemplate(MoneyRequestUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MoneyRequestUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    moneyRequestFormService = TestBed.inject(MoneyRequestFormService);
    moneyRequestService = TestBed.inject(MoneyRequestService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const moneyRequest: IMoneyRequest = { id: 456 };

      activatedRoute.data = of({ moneyRequest });
      comp.ngOnInit();

      expect(comp.moneyRequest).toEqual(moneyRequest);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMoneyRequest>>();
      const moneyRequest = { id: 123 };
      jest.spyOn(moneyRequestFormService, 'getMoneyRequest').mockReturnValue(moneyRequest);
      jest.spyOn(moneyRequestService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ moneyRequest });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: moneyRequest }));
      saveSubject.complete();

      // THEN
      expect(moneyRequestFormService.getMoneyRequest).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(moneyRequestService.update).toHaveBeenCalledWith(expect.objectContaining(moneyRequest));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMoneyRequest>>();
      const moneyRequest = { id: 123 };
      jest.spyOn(moneyRequestFormService, 'getMoneyRequest').mockReturnValue({ id: null });
      jest.spyOn(moneyRequestService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ moneyRequest: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: moneyRequest }));
      saveSubject.complete();

      // THEN
      expect(moneyRequestFormService.getMoneyRequest).toHaveBeenCalled();
      expect(moneyRequestService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMoneyRequest>>();
      const moneyRequest = { id: 123 };
      jest.spyOn(moneyRequestService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ moneyRequest });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(moneyRequestService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
