import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ServiceClientService } from '../service/service-client.service';
import { IServiceClient } from '../service-client.model';
import { ServiceClientFormService } from './service-client-form.service';

import { ServiceClientUpdateComponent } from './service-client-update.component';

describe('ServiceClient Management Update Component', () => {
  let comp: ServiceClientUpdateComponent;
  let fixture: ComponentFixture<ServiceClientUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let serviceClientFormService: ServiceClientFormService;
  let serviceClientService: ServiceClientService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ServiceClientUpdateComponent],
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
      .overrideTemplate(ServiceClientUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ServiceClientUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    serviceClientFormService = TestBed.inject(ServiceClientFormService);
    serviceClientService = TestBed.inject(ServiceClientService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const serviceClient: IServiceClient = { id: 456 };

      activatedRoute.data = of({ serviceClient });
      comp.ngOnInit();

      expect(comp.serviceClient).toEqual(serviceClient);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IServiceClient>>();
      const serviceClient = { id: 123 };
      jest.spyOn(serviceClientFormService, 'getServiceClient').mockReturnValue(serviceClient);
      jest.spyOn(serviceClientService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ serviceClient });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: serviceClient }));
      saveSubject.complete();

      // THEN
      expect(serviceClientFormService.getServiceClient).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(serviceClientService.update).toHaveBeenCalledWith(expect.objectContaining(serviceClient));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IServiceClient>>();
      const serviceClient = { id: 123 };
      jest.spyOn(serviceClientFormService, 'getServiceClient').mockReturnValue({ id: null });
      jest.spyOn(serviceClientService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ serviceClient: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: serviceClient }));
      saveSubject.complete();

      // THEN
      expect(serviceClientFormService.getServiceClient).toHaveBeenCalled();
      expect(serviceClientService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IServiceClient>>();
      const serviceClient = { id: 123 };
      jest.spyOn(serviceClientService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ serviceClient });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(serviceClientService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
