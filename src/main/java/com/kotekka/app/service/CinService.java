package com.kotekka.app.service;

import com.kotekka.app.domain.Cin;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.kotekka.app.domain.Cin}.
 */
public interface CinService {
    /**
     * Save a cin.
     *
     * @param cin the entity to save.
     * @return the persisted entity.
     */
    Cin save(Cin cin);

    /**
     * Updates a cin.
     *
     * @param cin the entity to update.
     * @return the persisted entity.
     */
    Cin update(Cin cin);

    /**
     * Partially updates a cin.
     *
     * @param cin the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Cin> partialUpdate(Cin cin);

    /**
     * Get the "id" cin.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Cin> findOne(Long id);

    /**
     * Delete the "id" cin.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
