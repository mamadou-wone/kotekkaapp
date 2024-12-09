package com.kotekka.app.service;

import com.kotekka.app.domain.Wallet;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.kotekka.app.domain.Wallet}.
 */
public interface WalletService {
    /**
     * Save a wallet.
     *
     * @param wallet the entity to save.
     * @return the persisted entity.
     */
    Wallet save(Wallet wallet);

    /**
     * Updates a wallet.
     *
     * @param wallet the entity to update.
     * @return the persisted entity.
     */
    Wallet update(Wallet wallet);

    /**
     * Partially updates a wallet.
     *
     * @param wallet the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Wallet> partialUpdate(Wallet wallet);

    /**
     * Get the "id" wallet.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Wallet> findOne(Long id);

    /**
     * Delete the "id" wallet.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
