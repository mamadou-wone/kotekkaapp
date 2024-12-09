package com.kotekka.app.service.impl;

import com.kotekka.app.domain.Device;
import com.kotekka.app.repository.DeviceRepository;
import com.kotekka.app.service.DeviceService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.kotekka.app.domain.Device}.
 */
@Service
@Transactional
public class DeviceServiceImpl implements DeviceService {

    private static final Logger LOG = LoggerFactory.getLogger(DeviceServiceImpl.class);

    private final DeviceRepository deviceRepository;

    public DeviceServiceImpl(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Override
    public Device save(Device device) {
        LOG.debug("Request to save Device : {}", device);
        return deviceRepository.save(device);
    }

    @Override
    public Device update(Device device) {
        LOG.debug("Request to update Device : {}", device);
        return deviceRepository.save(device);
    }

    @Override
    public Optional<Device> partialUpdate(Device device) {
        LOG.debug("Request to partially update Device : {}", device);

        return deviceRepository
            .findById(device.getId())
            .map(existingDevice -> {
                if (device.getUuid() != null) {
                    existingDevice.setUuid(device.getUuid());
                }
                if (device.getDeviceUuid() != null) {
                    existingDevice.setDeviceUuid(device.getDeviceUuid());
                }
                if (device.getType() != null) {
                    existingDevice.setType(device.getType());
                }
                if (device.getManufacturer() != null) {
                    existingDevice.setManufacturer(device.getManufacturer());
                }
                if (device.getModel() != null) {
                    existingDevice.setModel(device.getModel());
                }
                if (device.getOs() != null) {
                    existingDevice.setOs(device.getOs());
                }
                if (device.getAppVersion() != null) {
                    existingDevice.setAppVersion(device.getAppVersion());
                }
                if (device.getInactive() != null) {
                    existingDevice.setInactive(device.getInactive());
                }
                if (device.getCreatedBy() != null) {
                    existingDevice.setCreatedBy(device.getCreatedBy());
                }
                if (device.getCreatedDate() != null) {
                    existingDevice.setCreatedDate(device.getCreatedDate());
                }
                if (device.getLastModifiedBy() != null) {
                    existingDevice.setLastModifiedBy(device.getLastModifiedBy());
                }
                if (device.getLastModifiedDate() != null) {
                    existingDevice.setLastModifiedDate(device.getLastModifiedDate());
                }

                return existingDevice;
            })
            .map(deviceRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Device> findAll(Pageable pageable) {
        LOG.debug("Request to get all Devices");
        return deviceRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Device> findOne(Long id) {
        LOG.debug("Request to get Device : {}", id);
        return deviceRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Device : {}", id);
        deviceRepository.deleteById(id);
    }
}
