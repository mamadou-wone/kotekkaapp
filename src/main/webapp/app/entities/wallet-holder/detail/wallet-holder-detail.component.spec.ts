import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { WalletHolderDetailComponent } from './wallet-holder-detail.component';

describe('WalletHolder Management Detail Component', () => {
  let comp: WalletHolderDetailComponent;
  let fixture: ComponentFixture<WalletHolderDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [WalletHolderDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./wallet-holder-detail.component').then(m => m.WalletHolderDetailComponent),
              resolve: { walletHolder: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(WalletHolderDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(WalletHolderDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load walletHolder on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', WalletHolderDetailComponent);

      // THEN
      expect(instance.walletHolder()).toEqual(expect.objectContaining({ id: 123 }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
