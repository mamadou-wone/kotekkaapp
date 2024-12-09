package com.kotekka.app.service;

import com.kotekka.app.domain.UserPreference;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.kotekka.app.domain.UserPreference}.
 */
public interface UserPreferenceService {
    /**
     * Save a userPreference.
     *
     * @param userPreference the entity to save.
     * @return the persisted entity.
     */
    UserPreference save(UserPreference userPreference);

    /**
     * Updates a userPreference.
     *
     * @param userPreference the entity to update.
     * @return the persisted entity.
     */
    UserPreference update(UserPreference userPreference);

    /**
     * Partially updates a userPreference.
     *
     * @param userPreference the entity to update partially.
     * @return the persisted entity.
     */
    Optional<UserPreference> partialUpdate(UserPreference userPreference);

    /**
     * Get all the userPreferences with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UserPreference> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" userPreference.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UserPreference> findOne(Long id);

    /**
     * Delete the "id" userPreference.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
