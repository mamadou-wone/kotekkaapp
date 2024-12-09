package com.kotekka.app.service;

import com.kotekka.app.domain.UserAffiliation;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.kotekka.app.domain.UserAffiliation}.
 */
public interface UserAffiliationService {
    /**
     * Save a userAffiliation.
     *
     * @param userAffiliation the entity to save.
     * @return the persisted entity.
     */
    UserAffiliation save(UserAffiliation userAffiliation);

    /**
     * Updates a userAffiliation.
     *
     * @param userAffiliation the entity to update.
     * @return the persisted entity.
     */
    UserAffiliation update(UserAffiliation userAffiliation);

    /**
     * Partially updates a userAffiliation.
     *
     * @param userAffiliation the entity to update partially.
     * @return the persisted entity.
     */
    Optional<UserAffiliation> partialUpdate(UserAffiliation userAffiliation);

    /**
     * Get the "id" userAffiliation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UserAffiliation> findOne(Long id);

    /**
     * Delete the "id" userAffiliation.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
