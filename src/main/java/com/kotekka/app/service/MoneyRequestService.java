package com.kotekka.app.service;

import com.kotekka.app.domain.MoneyRequest;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.kotekka.app.domain.MoneyRequest}.
 */
public interface MoneyRequestService {
    /**
     * Save a moneyRequest.
     *
     * @param moneyRequest the entity to save.
     * @return the persisted entity.
     */
    MoneyRequest save(MoneyRequest moneyRequest);

    /**
     * Updates a moneyRequest.
     *
     * @param moneyRequest the entity to update.
     * @return the persisted entity.
     */
    MoneyRequest update(MoneyRequest moneyRequest);

    /**
     * Partially updates a moneyRequest.
     *
     * @param moneyRequest the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MoneyRequest> partialUpdate(MoneyRequest moneyRequest);

    /**
     * Get the "id" moneyRequest.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MoneyRequest> findOne(Long id);

    /**
     * Delete the "id" moneyRequest.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
