import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { WalletHolderService } from '../service/wallet-holder.service';
import { IWalletHolder } from '../wallet-holder.model';
import { WalletHolderFormService } from './wallet-holder-form.service';

import { WalletHolderUpdateComponent } from './wallet-holder-update.component';

describe('WalletHolder Management Update Component', () => {
  let comp: WalletHolderUpdateComponent;
  let fixture: ComponentFixture<WalletHolderUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let walletHolderFormService: WalletHolderFormService;
  let walletHolderService: WalletHolderService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [WalletHolderUpdateComponent],
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
      .overrideTemplate(WalletHolderUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(WalletHolderUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    walletHolderFormService = TestBed.inject(WalletHolderFormService);
    walletHolderService = TestBed.inject(WalletHolderService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const walletHolder: IWalletHolder = { id: 456 };
      const user: IUser = { id: 26519 };
      walletHolder.user = user;

      const userCollection: IUser[] = [{ id: 13987 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ walletHolder });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const walletHolder: IWalletHolder = { id: 456 };
      const user: IUser = { id: 22676 };
      walletHolder.user = user;

      activatedRoute.data = of({ walletHolder });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.walletHolder).toEqual(walletHolder);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWalletHolder>>();
      const walletHolder = { id: 123 };
      jest.spyOn(walletHolderFormService, 'getWalletHolder').mockReturnValue(walletHolder);
      jest.spyOn(walletHolderService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ walletHolder });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: walletHolder }));
      saveSubject.complete();

      // THEN
      expect(walletHolderFormService.getWalletHolder).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(walletHolderService.update).toHaveBeenCalledWith(expect.objectContaining(walletHolder));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWalletHolder>>();
      const walletHolder = { id: 123 };
      jest.spyOn(walletHolderFormService, 'getWalletHolder').mockReturnValue({ id: null });
      jest.spyOn(walletHolderService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ walletHolder: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: walletHolder }));
      saveSubject.complete();

      // THEN
      expect(walletHolderFormService.getWalletHolder).toHaveBeenCalled();
      expect(walletHolderService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWalletHolder>>();
      const walletHolder = { id: 123 };
      jest.spyOn(walletHolderService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ walletHolder });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(walletHolderService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUser', () => {
      it('Should forward to userService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
