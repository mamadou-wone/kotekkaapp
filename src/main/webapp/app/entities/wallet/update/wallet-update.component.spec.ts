import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { WalletService } from '../service/wallet.service';
import { IWallet } from '../wallet.model';
import { WalletFormService } from './wallet-form.service';

import { WalletUpdateComponent } from './wallet-update.component';

describe('Wallet Management Update Component', () => {
  let comp: WalletUpdateComponent;
  let fixture: ComponentFixture<WalletUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let walletFormService: WalletFormService;
  let walletService: WalletService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [WalletUpdateComponent],
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
      .overrideTemplate(WalletUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(WalletUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    walletFormService = TestBed.inject(WalletFormService);
    walletService = TestBed.inject(WalletService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const wallet: IWallet = { id: 456 };

      activatedRoute.data = of({ wallet });
      comp.ngOnInit();

      expect(comp.wallet).toEqual(wallet);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWallet>>();
      const wallet = { id: 123 };
      jest.spyOn(walletFormService, 'getWallet').mockReturnValue(wallet);
      jest.spyOn(walletService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ wallet });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: wallet }));
      saveSubject.complete();

      // THEN
      expect(walletFormService.getWallet).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(walletService.update).toHaveBeenCalledWith(expect.objectContaining(wallet));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWallet>>();
      const wallet = { id: 123 };
      jest.spyOn(walletFormService, 'getWallet').mockReturnValue({ id: null });
      jest.spyOn(walletService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ wallet: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: wallet }));
      saveSubject.complete();

      // THEN
      expect(walletFormService.getWallet).toHaveBeenCalled();
      expect(walletService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWallet>>();
      const wallet = { id: 123 };
      jest.spyOn(walletService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ wallet });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(walletService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
