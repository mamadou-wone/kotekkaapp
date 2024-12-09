package com.kotekka.app.service;

import com.kotekka.app.domain.IncomingCall;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.kotekka.app.domain.IncomingCall}.
 */
public interface IncomingCallService {
    /**
     * Save a incomingCall.
     *
     * @param incomingCall the entity to save.
     * @return the persisted entity.
     */
    IncomingCall save(IncomingCall incomingCall);

    /**
     * Updates a incomingCall.
     *
     * @param incomingCall the entity to update.
     * @return the persisted entity.
     */
    IncomingCall update(IncomingCall incomingCall);

    /**
     * Partially updates a incomingCall.
     *
     * @param incomingCall the entity to update partially.
     * @return the persisted entity.
     */
    Optional<IncomingCall> partialUpdate(IncomingCall incomingCall);

    /**
     * Get the "id" incomingCall.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<IncomingCall> findOne(Long id);

    /**
     * Delete the "id" incomingCall.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
