package com.kotekka.app.service;

import com.kotekka.app.domain.UserAccess;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.kotekka.app.domain.UserAccess}.
 */
public interface UserAccessService {
    /**
     * Save a userAccess.
     *
     * @param userAccess the entity to save.
     * @return the persisted entity.
     */
    UserAccess save(UserAccess userAccess);

    /**
     * Updates a userAccess.
     *
     * @param userAccess the entity to update.
     * @return the persisted entity.
     */
    UserAccess update(UserAccess userAccess);

    /**
     * Partially updates a userAccess.
     *
     * @param userAccess the entity to update partially.
     * @return the persisted entity.
     */
    Optional<UserAccess> partialUpdate(UserAccess userAccess);

    /**
     * Get the "id" userAccess.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UserAccess> findOne(Long id);

    /**
     * Delete the "id" userAccess.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
