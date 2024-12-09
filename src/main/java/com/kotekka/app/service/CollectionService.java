package com.kotekka.app.service;

import com.kotekka.app.domain.Collection;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.kotekka.app.domain.Collection}.
 */
public interface CollectionService {
    /**
     * Save a collection.
     *
     * @param collection the entity to save.
     * @return the persisted entity.
     */
    Collection save(Collection collection);

    /**
     * Updates a collection.
     *
     * @param collection the entity to update.
     * @return the persisted entity.
     */
    Collection update(Collection collection);

    /**
     * Partially updates a collection.
     *
     * @param collection the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Collection> partialUpdate(Collection collection);

    /**
     * Get the "id" collection.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Collection> findOne(Long id);

    /**
     * Delete the "id" collection.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
