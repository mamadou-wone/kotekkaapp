package com.kotekka.app.service;

import com.kotekka.app.domain.Review;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.kotekka.app.domain.Review}.
 */
public interface ReviewService {
    /**
     * Save a review.
     *
     * @param review the entity to save.
     * @return the persisted entity.
     */
    Review save(Review review);

    /**
     * Updates a review.
     *
     * @param review the entity to update.
     * @return the persisted entity.
     */
    Review update(Review review);

    /**
     * Partially updates a review.
     *
     * @param review the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Review> partialUpdate(Review review);

    /**
     * Get the "id" review.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Review> findOne(Long id);

    /**
     * Delete the "id" review.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
