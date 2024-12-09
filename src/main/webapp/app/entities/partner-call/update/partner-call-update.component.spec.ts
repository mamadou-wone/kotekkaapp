import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { PartnerCallService } from '../service/partner-call.service';
import { IPartnerCall } from '../partner-call.model';
import { PartnerCallFormService } from './partner-call-form.service';

import { PartnerCallUpdateComponent } from './partner-call-update.component';

describe('PartnerCall Management Update Component', () => {
  let comp: PartnerCallUpdateComponent;
  let fixture: ComponentFixture<PartnerCallUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let partnerCallFormService: PartnerCallFormService;
  let partnerCallService: PartnerCallService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [PartnerCallUpdateComponent],
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
      .overrideTemplate(PartnerCallUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PartnerCallUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    partnerCallFormService = TestBed.inject(PartnerCallFormService);
    partnerCallService = TestBed.inject(PartnerCallService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const partnerCall: IPartnerCall = { id: 456 };

      activatedRoute.data = of({ partnerCall });
      comp.ngOnInit();

      expect(comp.partnerCall).toEqual(partnerCall);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPartnerCall>>();
      const partnerCall = { id: 123 };
      jest.spyOn(partnerCallFormService, 'getPartnerCall').mockReturnValue(partnerCall);
      jest.spyOn(partnerCallService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ partnerCall });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: partnerCall }));
      saveSubject.complete();

      // THEN
      expect(partnerCallFormService.getPartnerCall).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(partnerCallService.update).toHaveBeenCalledWith(expect.objectContaining(partnerCall));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPartnerCall>>();
      const partnerCall = { id: 123 };
      jest.spyOn(partnerCallFormService, 'getPartnerCall').mockReturnValue({ id: null });
      jest.spyOn(partnerCallService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ partnerCall: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: partnerCall }));
      saveSubject.complete();

      // THEN
      expect(partnerCallFormService.getPartnerCall).toHaveBeenCalled();
      expect(partnerCallService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPartnerCall>>();
      const partnerCall = { id: 123 };
      jest.spyOn(partnerCallService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ partnerCall });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(partnerCallService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
