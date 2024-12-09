import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ReferalInfoService } from '../service/referal-info.service';
import { IReferalInfo } from '../referal-info.model';
import { ReferalInfoFormService } from './referal-info-form.service';

import { ReferalInfoUpdateComponent } from './referal-info-update.component';

describe('ReferalInfo Management Update Component', () => {
  let comp: ReferalInfoUpdateComponent;
  let fixture: ComponentFixture<ReferalInfoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let referalInfoFormService: ReferalInfoFormService;
  let referalInfoService: ReferalInfoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ReferalInfoUpdateComponent],
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
      .overrideTemplate(ReferalInfoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ReferalInfoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    referalInfoFormService = TestBed.inject(ReferalInfoFormService);
    referalInfoService = TestBed.inject(ReferalInfoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const referalInfo: IReferalInfo = { id: 456 };

      activatedRoute.data = of({ referalInfo });
      comp.ngOnInit();

      expect(comp.referalInfo).toEqual(referalInfo);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReferalInfo>>();
      const referalInfo = { id: 123 };
      jest.spyOn(referalInfoFormService, 'getReferalInfo').mockReturnValue(referalInfo);
      jest.spyOn(referalInfoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ referalInfo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: referalInfo }));
      saveSubject.complete();

      // THEN
      expect(referalInfoFormService.getReferalInfo).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(referalInfoService.update).toHaveBeenCalledWith(expect.objectContaining(referalInfo));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReferalInfo>>();
      const referalInfo = { id: 123 };
      jest.spyOn(referalInfoFormService, 'getReferalInfo').mockReturnValue({ id: null });
      jest.spyOn(referalInfoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ referalInfo: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: referalInfo }));
      saveSubject.complete();

      // THEN
      expect(referalInfoFormService.getReferalInfo).toHaveBeenCalled();
      expect(referalInfoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReferalInfo>>();
      const referalInfo = { id: 123 };
      jest.spyOn(referalInfoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ referalInfo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(referalInfoService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
