import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { RecipientService } from '../service/recipient.service';
import { IRecipient } from '../recipient.model';
import { RecipientFormService } from './recipient-form.service';

import { RecipientUpdateComponent } from './recipient-update.component';

describe('Recipient Management Update Component', () => {
  let comp: RecipientUpdateComponent;
  let fixture: ComponentFixture<RecipientUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let recipientFormService: RecipientFormService;
  let recipientService: RecipientService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RecipientUpdateComponent],
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
      .overrideTemplate(RecipientUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RecipientUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    recipientFormService = TestBed.inject(RecipientFormService);
    recipientService = TestBed.inject(RecipientService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const recipient: IRecipient = { id: 456 };

      activatedRoute.data = of({ recipient });
      comp.ngOnInit();

      expect(comp.recipient).toEqual(recipient);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRecipient>>();
      const recipient = { id: 123 };
      jest.spyOn(recipientFormService, 'getRecipient').mockReturnValue(recipient);
      jest.spyOn(recipientService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ recipient });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: recipient }));
      saveSubject.complete();

      // THEN
      expect(recipientFormService.getRecipient).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(recipientService.update).toHaveBeenCalledWith(expect.objectContaining(recipient));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRecipient>>();
      const recipient = { id: 123 };
      jest.spyOn(recipientFormService, 'getRecipient').mockReturnValue({ id: null });
      jest.spyOn(recipientService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ recipient: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: recipient }));
      saveSubject.complete();

      // THEN
      expect(recipientFormService.getRecipient).toHaveBeenCalled();
      expect(recipientService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRecipient>>();
      const recipient = { id: 123 };
      jest.spyOn(recipientService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ recipient });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(recipientService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
