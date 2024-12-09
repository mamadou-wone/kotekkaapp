package com.kotekka.app.service;

import com.kotekka.app.domain.OneTimePassword;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.kotekka.app.domain.OneTimePassword}.
 */
public interface OneTimePasswordService {
    /**
     * Save a oneTimePassword.
     *
     * @param oneTimePassword the entity to save.
     * @return the persisted entity.
     */
    OneTimePassword save(OneTimePassword oneTimePassword);

    /**
     * Updates a oneTimePassword.
     *
     * @param oneTimePassword the entity to update.
     * @return the persisted entity.
     */
    OneTimePassword update(OneTimePassword oneTimePassword);

    /**
     * Partially updates a oneTimePassword.
     *
     * @param oneTimePassword the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OneTimePassword> partialUpdate(OneTimePassword oneTimePassword);

    /**
     * Get the "id" oneTimePassword.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OneTimePassword> findOne(Long id);

    /**
     * Delete the "id" oneTimePassword.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
