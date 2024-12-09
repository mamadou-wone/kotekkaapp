package com.kotekka.app.service;

import com.kotekka.app.domain.FailedAttempt;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.kotekka.app.domain.FailedAttempt}.
 */
public interface FailedAttemptService {
    /**
     * Save a failedAttempt.
     *
     * @param failedAttempt the entity to save.
     * @return the persisted entity.
     */
    FailedAttempt save(FailedAttempt failedAttempt);

    /**
     * Updates a failedAttempt.
     *
     * @param failedAttempt the entity to update.
     * @return the persisted entity.
     */
    FailedAttempt update(FailedAttempt failedAttempt);

    /**
     * Partially updates a failedAttempt.
     *
     * @param failedAttempt the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FailedAttempt> partialUpdate(FailedAttempt failedAttempt);

    /**
     * Get the "id" failedAttempt.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FailedAttempt> findOne(Long id);

    /**
     * Delete the "id" failedAttempt.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
