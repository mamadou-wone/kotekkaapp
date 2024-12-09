package com.kotekka.app.service;

import com.kotekka.app.domain.ServiceClient;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.kotekka.app.domain.ServiceClient}.
 */
public interface ServiceClientService {
    /**
     * Save a serviceClient.
     *
     * @param serviceClient the entity to save.
     * @return the persisted entity.
     */
    ServiceClient save(ServiceClient serviceClient);

    /**
     * Updates a serviceClient.
     *
     * @param serviceClient the entity to update.
     * @return the persisted entity.
     */
    ServiceClient update(ServiceClient serviceClient);

    /**
     * Partially updates a serviceClient.
     *
     * @param serviceClient the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ServiceClient> partialUpdate(ServiceClient serviceClient);

    /**
     * Get the "id" serviceClient.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ServiceClient> findOne(Long id);

    /**
     * Delete the "id" serviceClient.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
