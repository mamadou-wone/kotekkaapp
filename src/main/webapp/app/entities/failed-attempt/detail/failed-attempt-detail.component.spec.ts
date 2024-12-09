import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { FailedAttemptDetailComponent } from './failed-attempt-detail.component';

describe('FailedAttempt Management Detail Component', () => {
  let comp: FailedAttemptDetailComponent;
  let fixture: ComponentFixture<FailedAttemptDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FailedAttemptDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./failed-attempt-detail.component').then(m => m.FailedAttemptDetailComponent),
              resolve: { failedAttempt: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(FailedAttemptDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FailedAttemptDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load failedAttempt on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', FailedAttemptDetailComponent);

      // THEN
      expect(instance.failedAttempt()).toEqual(expect.objectContaining({ id: 123 }));
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
