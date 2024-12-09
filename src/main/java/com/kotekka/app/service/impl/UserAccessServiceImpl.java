package com.kotekka.app.service.impl;

import com.kotekka.app.domain.UserAccess;
import com.kotekka.app.repository.UserAccessRepository;
import com.kotekka.app.service.UserAccessService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.kotekka.app.domain.UserAccess}.
 */
@Service
@Transactional
public class UserAccessServiceImpl implements UserAccessService {

    private static final Logger LOG = LoggerFactory.getLogger(UserAccessServiceImpl.class);

    private final UserAccessRepository userAccessRepository;

    public UserAccessServiceImpl(UserAccessRepository userAccessRepository) {
        this.userAccessRepository = userAccessRepository;
    }

    @Override
    public UserAccess save(UserAccess userAccess) {
        LOG.debug("Request to save UserAccess : {}", userAccess);
        return userAccessRepository.save(userAccess);
    }

    @Override
    public UserAccess update(UserAccess userAccess) {
        LOG.debug("Request to update UserAccess : {}", userAccess);
        return userAccessRepository.save(userAccess);
    }

    @Override
    public Optional<UserAccess> partialUpdate(UserAccess userAccess) {
        LOG.debug("Request to partially update UserAccess : {}", userAccess);

        return userAccessRepository
            .findById(userAccess.getId())
            .map(existingUserAccess -> {
                if (userAccess.getLogin() != null) {
                    existingUserAccess.setLogin(userAccess.getLogin());
                }
                if (userAccess.getIpAddress() != null) {
                    existingUserAccess.setIpAddress(userAccess.getIpAddress());
                }
                if (userAccess.getDevice() != null) {
                    existingUserAccess.setDevice(userAccess.getDevice());
                }
                if (userAccess.getCreatedDate() != null) {
                    existingUserAccess.setCreatedDate(userAccess.getCreatedDate());
                }

                return existingUserAccess;
            })
            .map(userAccessRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserAccess> findOne(Long id) {
        LOG.debug("Request to get UserAccess : {}", id);
        return userAccessRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete UserAccess : {}", id);
        userAccessRepository.deleteById(id);
    }
}
