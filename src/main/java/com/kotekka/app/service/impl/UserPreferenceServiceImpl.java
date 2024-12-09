package com.kotekka.app.service.impl;

import com.kotekka.app.domain.UserPreference;
import com.kotekka.app.repository.UserPreferenceRepository;
import com.kotekka.app.service.UserPreferenceService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.kotekka.app.domain.UserPreference}.
 */
@Service
@Transactional
public class UserPreferenceServiceImpl implements UserPreferenceService {

    private static final Logger LOG = LoggerFactory.getLogger(UserPreferenceServiceImpl.class);

    private final UserPreferenceRepository userPreferenceRepository;

    public UserPreferenceServiceImpl(UserPreferenceRepository userPreferenceRepository) {
        this.userPreferenceRepository = userPreferenceRepository;
    }

    @Override
    public UserPreference save(UserPreference userPreference) {
        LOG.debug("Request to save UserPreference : {}", userPreference);
        return userPreferenceRepository.save(userPreference);
    }

    @Override
    public UserPreference update(UserPreference userPreference) {
        LOG.debug("Request to update UserPreference : {}", userPreference);
        return userPreferenceRepository.save(userPreference);
    }

    @Override
    public Optional<UserPreference> partialUpdate(UserPreference userPreference) {
        LOG.debug("Request to partially update UserPreference : {}", userPreference);

        return userPreferenceRepository
            .findById(userPreference.getId())
            .map(existingUserPreference -> {
                if (userPreference.getApp() != null) {
                    existingUserPreference.setApp(userPreference.getApp());
                }
                if (userPreference.getName() != null) {
                    existingUserPreference.setName(userPreference.getName());
                }
                if (userPreference.getSetting() != null) {
                    existingUserPreference.setSetting(userPreference.getSetting());
                }
                if (userPreference.getCreatedDate() != null) {
                    existingUserPreference.setCreatedDate(userPreference.getCreatedDate());
                }

                return existingUserPreference;
            })
            .map(userPreferenceRepository::save);
    }

    public Page<UserPreference> findAllWithEagerRelationships(Pageable pageable) {
        return userPreferenceRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserPreference> findOne(Long id) {
        LOG.debug("Request to get UserPreference : {}", id);
        return userPreferenceRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete UserPreference : {}", id);
        userPreferenceRepository.deleteById(id);
    }
}
