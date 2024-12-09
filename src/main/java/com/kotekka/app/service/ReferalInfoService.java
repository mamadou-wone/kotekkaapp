package com.kotekka.app.service;

import com.kotekka.app.domain.ReferalInfo;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.kotekka.app.domain.ReferalInfo}.
 */
public interface ReferalInfoService {
    /**
     * Save a referalInfo.
     *
     * @param referalInfo the entity to save.
     * @return the persisted entity.
     */
    ReferalInfo save(ReferalInfo referalInfo);

    /**
     * Updates a referalInfo.
     *
     * @param referalInfo the entity to update.
     * @return the persisted entity.
     */
    ReferalInfo update(ReferalInfo referalInfo);

    /**
     * Partially updates a referalInfo.
     *
     * @param referalInfo the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ReferalInfo> partialUpdate(ReferalInfo referalInfo);

    /**
     * Get the "id" referalInfo.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ReferalInfo> findOne(Long id);

    /**
     * Delete the "id" referalInfo.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
