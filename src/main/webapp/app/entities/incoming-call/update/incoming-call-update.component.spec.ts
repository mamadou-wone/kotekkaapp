import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IncomingCallService } from '../service/incoming-call.service';
import { IIncomingCall } from '../incoming-call.model';
import { IncomingCallFormService } from './incoming-call-form.service';

import { IncomingCallUpdateComponent } from './incoming-call-update.component';

describe('IncomingCall Management Update Component', () => {
  let comp: IncomingCallUpdateComponent;
  let fixture: ComponentFixture<IncomingCallUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let incomingCallFormService: IncomingCallFormService;
  let incomingCallService: IncomingCallService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [IncomingCallUpdateComponent],
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
      .overrideTemplate(IncomingCallUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(IncomingCallUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    incomingCallFormService = TestBed.inject(IncomingCallFormService);
    incomingCallService = TestBed.inject(IncomingCallService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const incomingCall: IIncomingCall = { id: 456 };

      activatedRoute.data = of({ incomingCall });
      comp.ngOnInit();

      expect(comp.incomingCall).toEqual(incomingCall);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IIncomingCall>>();
      const incomingCall = { id: 123 };
      jest.spyOn(incomingCallFormService, 'getIncomingCall').mockReturnValue(incomingCall);
      jest.spyOn(incomingCallService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ incomingCall });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: incomingCall }));
      saveSubject.complete();

      // THEN
      expect(incomingCallFormService.getIncomingCall).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(incomingCallService.update).toHaveBeenCalledWith(expect.objectContaining(incomingCall));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IIncomingCall>>();
      const incomingCall = { id: 123 };
      jest.spyOn(incomingCallFormService, 'getIncomingCall').mockReturnValue({ id: null });
      jest.spyOn(incomingCallService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ incomingCall: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: incomingCall }));
      saveSubject.complete();

      // THEN
      expect(incomingCallFormService.getIncomingCall).toHaveBeenCalled();
      expect(incomingCallService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IIncomingCall>>();
      const incomingCall = { id: 123 };
      jest.spyOn(incomingCallService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ incomingCall });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(incomingCallService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
