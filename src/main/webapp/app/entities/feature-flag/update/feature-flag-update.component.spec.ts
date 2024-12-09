import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { FeatureFlagService } from '../service/feature-flag.service';
import { IFeatureFlag } from '../feature-flag.model';
import { FeatureFlagFormService } from './feature-flag-form.service';

import { FeatureFlagUpdateComponent } from './feature-flag-update.component';

describe('FeatureFlag Management Update Component', () => {
  let comp: FeatureFlagUpdateComponent;
  let fixture: ComponentFixture<FeatureFlagUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let featureFlagFormService: FeatureFlagFormService;
  let featureFlagService: FeatureFlagService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [FeatureFlagUpdateComponent],
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
      .overrideTemplate(FeatureFlagUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FeatureFlagUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    featureFlagFormService = TestBed.inject(FeatureFlagFormService);
    featureFlagService = TestBed.inject(FeatureFlagService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const featureFlag: IFeatureFlag = { id: 456 };

      activatedRoute.data = of({ featureFlag });
      comp.ngOnInit();

      expect(comp.featureFlag).toEqual(featureFlag);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFeatureFlag>>();
      const featureFlag = { id: 123 };
      jest.spyOn(featureFlagFormService, 'getFeatureFlag').mockReturnValue(featureFlag);
      jest.spyOn(featureFlagService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ featureFlag });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: featureFlag }));
      saveSubject.complete();

      // THEN
      expect(featureFlagFormService.getFeatureFlag).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(featureFlagService.update).toHaveBeenCalledWith(expect.objectContaining(featureFlag));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFeatureFlag>>();
      const featureFlag = { id: 123 };
      jest.spyOn(featureFlagFormService, 'getFeatureFlag').mockReturnValue({ id: null });
      jest.spyOn(featureFlagService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ featureFlag: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: featureFlag }));
      saveSubject.complete();

      // THEN
      expect(featureFlagFormService.getFeatureFlag).toHaveBeenCalled();
      expect(featureFlagService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFeatureFlag>>();
      const featureFlag = { id: 123 };
      jest.spyOn(featureFlagService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ featureFlag });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(featureFlagService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
