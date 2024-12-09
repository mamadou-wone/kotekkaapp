package com.kotekka.app.service;

import com.kotekka.app.domain.PartnerCall;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.kotekka.app.domain.PartnerCall}.
 */
public interface PartnerCallService {
    /**
     * Save a partnerCall.
     *
     * @param partnerCall the entity to save.
     * @return the persisted entity.
     */
    PartnerCall save(PartnerCall partnerCall);

    /**
     * Updates a partnerCall.
     *
     * @param partnerCall the entity to update.
     * @return the persisted entity.
     */
    PartnerCall update(PartnerCall partnerCall);

    /**
     * Partially updates a partnerCall.
     *
     * @param partnerCall the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PartnerCall> partialUpdate(PartnerCall partnerCall);

    /**
     * Get the "id" partnerCall.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PartnerCall> findOne(Long id);

    /**
     * Delete the "id" partnerCall.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
