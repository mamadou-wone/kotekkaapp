import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { ICartItem } from '../cart-item.model';

@Component({
  standalone: true,
  selector: 'jhi-cart-item-detail',
  templateUrl: './cart-item-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class CartItemDetailComponent {
  cartItem = input<ICartItem | null>(null);

  previousState(): void {
    window.history.back();
  }
}
