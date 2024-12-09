import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { ICollection } from '../collection.model';
import { CollectionService } from '../service/collection.service';
import { CollectionFormGroup, CollectionFormService } from './collection-form.service';

@Component({
  standalone: true,
  selector: 'jhi-collection-update',
  templateUrl: './collection-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CollectionUpdateComponent implements OnInit {
  isSaving = false;
  collection: ICollection | null = null;

  productsSharedCollection: IProduct[] = [];

  protected collectionService = inject(CollectionService);
  protected collectionFormService = inject(CollectionFormService);
  protected productService = inject(ProductService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CollectionFormGroup = this.collectionFormService.createCollectionFormGroup();

  compareProduct = (o1: IProduct | null, o2: IProduct | null): boolean => this.productService.compareProduct(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ collection }) => {
      this.collection = collection;
      if (collection) {
        this.updateForm(collection);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const collection = this.collectionFormService.getCollection(this.editForm);
    if (collection.id !== null) {
      this.subscribeToSaveResponse(this.collectionService.update(collection));
    } else {
      this.subscribeToSaveResponse(this.collectionService.create(collection));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICollection>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(collection: ICollection): void {
    this.collection = collection;
    this.collectionFormService.resetForm(this.editForm, collection);

    this.productsSharedCollection = this.productService.addProductToCollectionIfMissing<IProduct>(
      this.productsSharedCollection,
      ...(collection.products ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(
        map((products: IProduct[]) =>
          this.productService.addProductToCollectionIfMissing<IProduct>(products, ...(this.collection?.products ?? [])),
        ),
      )
      .subscribe((products: IProduct[]) => (this.productsSharedCollection = products));
  }
}
