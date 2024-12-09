import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { OneTimePasswordDetailComponent } from './one-time-password-detail.component';

describe('OneTimePassword Management Detail Component', () => {
  let comp: OneTimePasswordDetailComponent;
  let fixture: ComponentFixture<OneTimePasswordDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OneTimePasswordDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./one-time-password-detail.component').then(m => m.OneTimePasswordDetailComponent),
              resolve: { oneTimePassword: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(OneTimePasswordDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(OneTimePasswordDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load oneTimePassword on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', OneTimePasswordDetailComponent);

      // THEN
      expect(instance.oneTimePassword()).toEqual(expect.objectContaining({ id: 123 }));
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
