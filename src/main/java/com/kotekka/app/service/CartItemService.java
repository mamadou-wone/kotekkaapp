package com.kotekka.app.service;

import com.kotekka.app.domain.CartItem;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.kotekka.app.domain.CartItem}.
 */
public interface CartItemService {
    /**
     * Save a cartItem.
     *
     * @param cartItem the entity to save.
     * @return the persisted entity.
     */
    CartItem save(CartItem cartItem);

    /**
     * Updates a cartItem.
     *
     * @param cartItem the entity to update.
     * @return the persisted entity.
     */
    CartItem update(CartItem cartItem);

    /**
     * Partially updates a cartItem.
     *
     * @param cartItem the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CartItem> partialUpdate(CartItem cartItem);

    /**
     * Get the "id" cartItem.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CartItem> findOne(Long id);

    /**
     * Delete the "id" cartItem.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
