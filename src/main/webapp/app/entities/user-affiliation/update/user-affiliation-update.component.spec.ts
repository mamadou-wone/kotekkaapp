import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { UserAffiliationService } from '../service/user-affiliation.service';
import { IUserAffiliation } from '../user-affiliation.model';
import { UserAffiliationFormService } from './user-affiliation-form.service';

import { UserAffiliationUpdateComponent } from './user-affiliation-update.component';

describe('UserAffiliation Management Update Component', () => {
  let comp: UserAffiliationUpdateComponent;
  let fixture: ComponentFixture<UserAffiliationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let userAffiliationFormService: UserAffiliationFormService;
  let userAffiliationService: UserAffiliationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [UserAffiliationUpdateComponent],
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
      .overrideTemplate(UserAffiliationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(UserAffiliationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    userAffiliationFormService = TestBed.inject(UserAffiliationFormService);
    userAffiliationService = TestBed.inject(UserAffiliationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const userAffiliation: IUserAffiliation = { id: 456 };

      activatedRoute.data = of({ userAffiliation });
      comp.ngOnInit();

      expect(comp.userAffiliation).toEqual(userAffiliation);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserAffiliation>>();
      const userAffiliation = { id: 123 };
      jest.spyOn(userAffiliationFormService, 'getUserAffiliation').mockReturnValue(userAffiliation);
      jest.spyOn(userAffiliationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userAffiliation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: userAffiliation }));
      saveSubject.complete();

      // THEN
      expect(userAffiliationFormService.getUserAffiliation).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(userAffiliationService.update).toHaveBeenCalledWith(expect.objectContaining(userAffiliation));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserAffiliation>>();
      const userAffiliation = { id: 123 };
      jest.spyOn(userAffiliationFormService, 'getUserAffiliation').mockReturnValue({ id: null });
      jest.spyOn(userAffiliationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userAffiliation: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: userAffiliation }));
      saveSubject.complete();

      // THEN
      expect(userAffiliationFormService.getUserAffiliation).toHaveBeenCalled();
      expect(userAffiliationService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserAffiliation>>();
      const userAffiliation = { id: 123 };
      jest.spyOn(userAffiliationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userAffiliation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(userAffiliationService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
