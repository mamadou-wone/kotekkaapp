package com.kotekka.app.service;

import com.kotekka.app.domain.WalletHolder;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.kotekka.app.domain.WalletHolder}.
 */
public interface WalletHolderService {
    /**
     * Save a walletHolder.
     *
     * @param walletHolder the entity to save.
     * @return the persisted entity.
     */
    WalletHolder save(WalletHolder walletHolder);

    /**
     * Updates a walletHolder.
     *
     * @param walletHolder the entity to update.
     * @return the persisted entity.
     */
    WalletHolder update(WalletHolder walletHolder);

    /**
     * Partially updates a walletHolder.
     *
     * @param walletHolder the entity to update partially.
     * @return the persisted entity.
     */
    Optional<WalletHolder> partialUpdate(WalletHolder walletHolder);

    /**
     * Get all the walletHolders with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<WalletHolder> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" walletHolder.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<WalletHolder> findOne(Long id);

    /**
     * Delete the "id" walletHolder.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
