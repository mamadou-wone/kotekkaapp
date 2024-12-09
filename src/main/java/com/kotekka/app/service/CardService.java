package com.kotekka.app.service;

import com.kotekka.app.domain.Card;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.kotekka.app.domain.Card}.
 */
public interface CardService {
    /**
     * Save a card.
     *
     * @param card the entity to save.
     * @return the persisted entity.
     */
    Card save(Card card);

    /**
     * Updates a card.
     *
     * @param card the entity to update.
     * @return the persisted entity.
     */
    Card update(Card card);

    /**
     * Partially updates a card.
     *
     * @param card the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Card> partialUpdate(Card card);

    /**
     * Get all the cards.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Card> findAll(Pageable pageable);

    /**
     * Get the "id" card.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Card> findOne(Long id);

    /**
     * Delete the "id" card.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
