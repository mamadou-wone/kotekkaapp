package com.kotekka.app.service;

import com.kotekka.app.domain.CacheInfo;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.kotekka.app.domain.CacheInfo}.
 */
public interface CacheInfoService {
    /**
     * Save a cacheInfo.
     *
     * @param cacheInfo the entity to save.
     * @return the persisted entity.
     */
    CacheInfo save(CacheInfo cacheInfo);

    /**
     * Updates a cacheInfo.
     *
     * @param cacheInfo the entity to update.
     * @return the persisted entity.
     */
    CacheInfo update(CacheInfo cacheInfo);

    /**
     * Partially updates a cacheInfo.
     *
     * @param cacheInfo the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CacheInfo> partialUpdate(CacheInfo cacheInfo);

    /**
     * Get the "id" cacheInfo.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CacheInfo> findOne(Long id);

    /**
     * Delete the "id" cacheInfo.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
