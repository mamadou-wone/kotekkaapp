package com.kotekka.app.service;

import com.kotekka.app.domain.Recipient;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.kotekka.app.domain.Recipient}.
 */
public interface RecipientService {
    /**
     * Save a recipient.
     *
     * @param recipient the entity to save.
     * @return the persisted entity.
     */
    Recipient save(Recipient recipient);

    /**
     * Updates a recipient.
     *
     * @param recipient the entity to update.
     * @return the persisted entity.
     */
    Recipient update(Recipient recipient);

    /**
     * Partially updates a recipient.
     *
     * @param recipient the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Recipient> partialUpdate(Recipient recipient);

    /**
     * Get all the recipients.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Recipient> findAll(Pageable pageable);

    /**
     * Get the "id" recipient.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Recipient> findOne(Long id);

    /**
     * Delete the "id" recipient.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
