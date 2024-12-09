import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { BeneficiaryService } from '../service/beneficiary.service';
import { IBeneficiary } from '../beneficiary.model';
import { BeneficiaryFormService } from './beneficiary-form.service';

import { BeneficiaryUpdateComponent } from './beneficiary-update.component';

describe('Beneficiary Management Update Component', () => {
  let comp: BeneficiaryUpdateComponent;
  let fixture: ComponentFixture<BeneficiaryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let beneficiaryFormService: BeneficiaryFormService;
  let beneficiaryService: BeneficiaryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [BeneficiaryUpdateComponent],
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
      .overrideTemplate(BeneficiaryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BeneficiaryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    beneficiaryFormService = TestBed.inject(BeneficiaryFormService);
    beneficiaryService = TestBed.inject(BeneficiaryService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const beneficiary: IBeneficiary = { id: 456 };

      activatedRoute.data = of({ beneficiary });
      comp.ngOnInit();

      expect(comp.beneficiary).toEqual(beneficiary);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBeneficiary>>();
      const beneficiary = { id: 123 };
      jest.spyOn(beneficiaryFormService, 'getBeneficiary').mockReturnValue(beneficiary);
      jest.spyOn(beneficiaryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ beneficiary });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: beneficiary }));
      saveSubject.complete();

      // THEN
      expect(beneficiaryFormService.getBeneficiary).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(beneficiaryService.update).toHaveBeenCalledWith(expect.objectContaining(beneficiary));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBeneficiary>>();
      const beneficiary = { id: 123 };
      jest.spyOn(beneficiaryFormService, 'getBeneficiary').mockReturnValue({ id: null });
      jest.spyOn(beneficiaryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ beneficiary: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: beneficiary }));
      saveSubject.complete();

      // THEN
      expect(beneficiaryFormService.getBeneficiary).toHaveBeenCalled();
      expect(beneficiaryService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBeneficiary>>();
      const beneficiary = { id: 123 };
      jest.spyOn(beneficiaryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ beneficiary });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(beneficiaryService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
