package com.kotekka.app.service.impl;

import com.kotekka.app.domain.ServiceClient;
import com.kotekka.app.repository.ServiceClientRepository;
import com.kotekka.app.service.ServiceClientService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.kotekka.app.domain.ServiceClient}.
 */
@Service
@Transactional
public class ServiceClientServiceImpl implements ServiceClientService {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceClientServiceImpl.class);

    private final ServiceClientRepository serviceClientRepository;

    public ServiceClientServiceImpl(ServiceClientRepository serviceClientRepository) {
        this.serviceClientRepository = serviceClientRepository;
    }

    @Override
    public ServiceClient save(ServiceClient serviceClient) {
        LOG.debug("Request to save ServiceClient : {}", serviceClient);
        return serviceClientRepository.save(serviceClient);
    }

    @Override
    public ServiceClient update(ServiceClient serviceClient) {
        LOG.debug("Request to update ServiceClient : {}", serviceClient);
        return serviceClientRepository.save(serviceClient);
    }

    @Override
    public Optional<ServiceClient> partialUpdate(ServiceClient serviceClient) {
        LOG.debug("Request to partially update ServiceClient : {}", serviceClient);

        return serviceClientRepository
            .findById(serviceClient.getId())
            .map(existingServiceClient -> {
                if (serviceClient.getClientId() != null) {
                    existingServiceClient.setClientId(serviceClient.getClientId());
                }
                if (serviceClient.getType() != null) {
                    existingServiceClient.setType(serviceClient.getType());
                }
                if (serviceClient.getApiKey() != null) {
                    existingServiceClient.setApiKey(serviceClient.getApiKey());
                }
                if (serviceClient.getStatus() != null) {
                    existingServiceClient.setStatus(serviceClient.getStatus());
                }
                if (serviceClient.getNote() != null) {
                    existingServiceClient.setNote(serviceClient.getNote());
                }
                if (serviceClient.getCreatedDate() != null) {
                    existingServiceClient.setCreatedDate(serviceClient.getCreatedDate());
                }
                if (serviceClient.getLastModifiedDate() != null) {
                    existingServiceClient.setLastModifiedDate(serviceClient.getLastModifiedDate());
                }

                return existingServiceClient;
            })
            .map(serviceClientRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ServiceClient> findOne(Long id) {
        LOG.debug("Request to get ServiceClient : {}", id);
        return serviceClientRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ServiceClient : {}", id);
        serviceClientRepository.deleteById(id);
    }
}
