package com.kotekka.app.service;

import com.kotekka.app.domain.Notification;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.kotekka.app.domain.Notification}.
 */
public interface NotificationService {
    /**
     * Save a notification.
     *
     * @param notification the entity to save.
     * @return the persisted entity.
     */
    Notification save(Notification notification);

    /**
     * Updates a notification.
     *
     * @param notification the entity to update.
     * @return the persisted entity.
     */
    Notification update(Notification notification);

    /**
     * Partially updates a notification.
     *
     * @param notification the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Notification> partialUpdate(Notification notification);

    /**
     * Get all the notifications.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Notification> findAll(Pageable pageable);

    /**
     * Get the "id" notification.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Notification> findOne(Long id);

    /**
     * Delete the "id" notification.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
