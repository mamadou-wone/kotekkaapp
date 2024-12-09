import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICartItem } from '../cart-item.model';
import { CartItemService } from '../service/cart-item.service';
import { CartItemFormGroup, CartItemFormService } from './cart-item-form.service';

@Component({
  standalone: true,
  selector: 'jhi-cart-item-update',
  templateUrl: './cart-item-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CartItemUpdateComponent implements OnInit {
  isSaving = false;
  cartItem: ICartItem | null = null;

  protected cartItemService = inject(CartItemService);
  protected cartItemFormService = inject(CartItemFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CartItemFormGroup = this.cartItemFormService.createCartItemFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cartItem }) => {
      this.cartItem = cartItem;
      if (cartItem) {
        this.updateForm(cartItem);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cartItem = this.cartItemFormService.getCartItem(this.editForm);
    if (cartItem.id !== null) {
      this.subscribeToSaveResponse(this.cartItemService.update(cartItem));
    } else {
      this.subscribeToSaveResponse(this.cartItemService.create(cartItem));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICartItem>>): void {
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

  protected updateForm(cartItem: ICartItem): void {
    this.cartItem = cartItem;
    this.cartItemFormService.resetForm(this.editForm, cartItem);
  }
}
