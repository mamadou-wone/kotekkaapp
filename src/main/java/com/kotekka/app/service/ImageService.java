package com.kotekka.app.service;

import com.kotekka.app.domain.Image;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.kotekka.app.domain.Image}.
 */
public interface ImageService {
    /**
     * Save a image.
     *
     * @param image the entity to save.
     * @return the persisted entity.
     */
    Image save(Image image);

    /**
     * Updates a image.
     *
     * @param image the entity to update.
     * @return the persisted entity.
     */
    Image update(Image image);

    /**
     * Partially updates a image.
     *
     * @param image the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Image> partialUpdate(Image image);

    /**
     * Get the "id" image.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Image> findOne(Long id);

    /**
     * Delete the "id" image.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
