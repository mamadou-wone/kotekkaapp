package com.kotekka.app.service;

import com.kotekka.app.domain.Beneficiary;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.kotekka.app.domain.Beneficiary}.
 */
public interface BeneficiaryService {
    /**
     * Save a beneficiary.
     *
     * @param beneficiary the entity to save.
     * @return the persisted entity.
     */
    Beneficiary save(Beneficiary beneficiary);

    /**
     * Updates a beneficiary.
     *
     * @param beneficiary the entity to update.
     * @return the persisted entity.
     */
    Beneficiary update(Beneficiary beneficiary);

    /**
     * Partially updates a beneficiary.
     *
     * @param beneficiary the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Beneficiary> partialUpdate(Beneficiary beneficiary);

    /**
     * Get all the beneficiaries.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Beneficiary> findAll(Pageable pageable);

    /**
     * Get the "id" beneficiary.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Beneficiary> findOne(Long id);

    /**
     * Delete the "id" beneficiary.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
