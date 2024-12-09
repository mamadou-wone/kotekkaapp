import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { UserAccessService } from '../service/user-access.service';
import { IUserAccess } from '../user-access.model';
import { UserAccessFormService } from './user-access-form.service';

import { UserAccessUpdateComponent } from './user-access-update.component';

describe('UserAccess Management Update Component', () => {
  let comp: UserAccessUpdateComponent;
  let fixture: ComponentFixture<UserAccessUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let userAccessFormService: UserAccessFormService;
  let userAccessService: UserAccessService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [UserAccessUpdateComponent],
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
      .overrideTemplate(UserAccessUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(UserAccessUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    userAccessFormService = TestBed.inject(UserAccessFormService);
    userAccessService = TestBed.inject(UserAccessService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const userAccess: IUserAccess = { id: 456 };

      activatedRoute.data = of({ userAccess });
      comp.ngOnInit();

      expect(comp.userAccess).toEqual(userAccess);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserAccess>>();
      const userAccess = { id: 123 };
      jest.spyOn(userAccessFormService, 'getUserAccess').mockReturnValue(userAccess);
      jest.spyOn(userAccessService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userAccess });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: userAccess }));
      saveSubject.complete();

      // THEN
      expect(userAccessFormService.getUserAccess).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(userAccessService.update).toHaveBeenCalledWith(expect.objectContaining(userAccess));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserAccess>>();
      const userAccess = { id: 123 };
      jest.spyOn(userAccessFormService, 'getUserAccess').mockReturnValue({ id: null });
      jest.spyOn(userAccessService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userAccess: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: userAccess }));
      saveSubject.complete();

      // THEN
      expect(userAccessFormService.getUserAccess).toHaveBeenCalled();
      expect(userAccessService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserAccess>>();
      const userAccess = { id: 123 };
      jest.spyOn(userAccessService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userAccess });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(userAccessService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
