package com.kotekka.app.service;

import com.kotekka.app.domain.Device;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.kotekka.app.domain.Device}.
 */
public interface DeviceService {
    /**
     * Save a device.
     *
     * @param device the entity to save.
     * @return the persisted entity.
     */
    Device save(Device device);

    /**
     * Updates a device.
     *
     * @param device the entity to update.
     * @return the persisted entity.
     */
    Device update(Device device);

    /**
     * Partially updates a device.
     *
     * @param device the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Device> partialUpdate(Device device);

    /**
     * Get all the devices.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Device> findAll(Pageable pageable);

    /**
     * Get the "id" device.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Device> findOne(Long id);

    /**
     * Delete the "id" device.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
