package com.kotekka.app.service;

import com.kotekka.app.domain.FeatureFlag;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.kotekka.app.domain.FeatureFlag}.
 */
public interface FeatureFlagService {
    /**
     * Save a featureFlag.
     *
     * @param featureFlag the entity to save.
     * @return the persisted entity.
     */
    FeatureFlag save(FeatureFlag featureFlag);

    /**
     * Updates a featureFlag.
     *
     * @param featureFlag the entity to update.
     * @return the persisted entity.
     */
    FeatureFlag update(FeatureFlag featureFlag);

    /**
     * Partially updates a featureFlag.
     *
     * @param featureFlag the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FeatureFlag> partialUpdate(FeatureFlag featureFlag);

    /**
     * Get the "id" featureFlag.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FeatureFlag> findOne(Long id);

    /**
     * Delete the "id" featureFlag.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
