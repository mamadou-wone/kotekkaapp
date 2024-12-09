import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { CardService } from '../service/card.service';
import { ICard } from '../card.model';
import { CardFormService } from './card-form.service';

import { CardUpdateComponent } from './card-update.component';

describe('Card Management Update Component', () => {
  let comp: CardUpdateComponent;
  let fixture: ComponentFixture<CardUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let cardFormService: CardFormService;
  let cardService: CardService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CardUpdateComponent],
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
      .overrideTemplate(CardUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CardUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    cardFormService = TestBed.inject(CardFormService);
    cardService = TestBed.inject(CardService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const card: ICard = { id: 456 };

      activatedRoute.data = of({ card });
      comp.ngOnInit();

      expect(comp.card).toEqual(card);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICard>>();
      const card = { id: 123 };
      jest.spyOn(cardFormService, 'getCard').mockReturnValue(card);
      jest.spyOn(cardService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ card });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: card }));
      saveSubject.complete();

      // THEN
      expect(cardFormService.getCard).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(cardService.update).toHaveBeenCalledWith(expect.objectContaining(card));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICard>>();
      const card = { id: 123 };
      jest.spyOn(cardFormService, 'getCard').mockReturnValue({ id: null });
      jest.spyOn(cardService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ card: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: card }));
      saveSubject.complete();

      // THEN
      expect(cardFormService.getCard).toHaveBeenCalled();
      expect(cardService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICard>>();
      const card = { id: 123 };
      jest.spyOn(cardService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ card });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(cardService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
