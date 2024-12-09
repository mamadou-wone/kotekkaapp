package com.kotekka.app.service;

import com.kotekka.app.domain.FailedAttemptHistory;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.kotekka.app.domain.FailedAttemptHistory}.
 */
public interface FailedAttemptHistoryService {
    /**
     * Save a failedAttemptHistory.
     *
     * @param failedAttemptHistory the entity to save.
     * @return the persisted entity.
     */
    FailedAttemptHistory save(FailedAttemptHistory failedAttemptHistory);

    /**
     * Updates a failedAttemptHistory.
     *
     * @param failedAttemptHistory the entity to update.
     * @return the persisted entity.
     */
    FailedAttemptHistory update(FailedAttemptHistory failedAttemptHistory);

    /**
     * Partially updates a failedAttemptHistory.
     *
     * @param failedAttemptHistory the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FailedAttemptHistory> partialUpdate(FailedAttemptHistory failedAttemptHistory);

    /**
     * Get all the failedAttemptHistories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FailedAttemptHistory> findAll(Pageable pageable);

    /**
     * Get the "id" failedAttemptHistory.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FailedAttemptHistory> findOne(Long id);

    /**
     * Delete the "id" failedAttemptHistory.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
