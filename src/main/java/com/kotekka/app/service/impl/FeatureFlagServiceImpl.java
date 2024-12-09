package com.kotekka.app.service.impl;

import com.kotekka.app.domain.FeatureFlag;
import com.kotekka.app.repository.FeatureFlagRepository;
import com.kotekka.app.service.FeatureFlagService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.kotekka.app.domain.FeatureFlag}.
 */
@Service
@Transactional
public class FeatureFlagServiceImpl implements FeatureFlagService {

    private static final Logger LOG = LoggerFactory.getLogger(FeatureFlagServiceImpl.class);

    private final FeatureFlagRepository featureFlagRepository;

    public FeatureFlagServiceImpl(FeatureFlagRepository featureFlagRepository) {
        this.featureFlagRepository = featureFlagRepository;
    }

    @Override
    public FeatureFlag save(FeatureFlag featureFlag) {
        LOG.debug("Request to save FeatureFlag : {}", featureFlag);
        return featureFlagRepository.save(featureFlag);
    }

    @Override
    public FeatureFlag update(FeatureFlag featureFlag) {
        LOG.debug("Request to update FeatureFlag : {}", featureFlag);
        return featureFlagRepository.save(featureFlag);
    }

    @Override
    public Optional<FeatureFlag> partialUpdate(FeatureFlag featureFlag) {
        LOG.debug("Request to partially update FeatureFlag : {}", featureFlag);

        return featureFlagRepository
            .findById(featureFlag.getId())
            .map(existingFeatureFlag -> {
                if (featureFlag.getName() != null) {
                    existingFeatureFlag.setName(featureFlag.getName());
                }
                if (featureFlag.getEnabled() != null) {
                    existingFeatureFlag.setEnabled(featureFlag.getEnabled());
                }
                if (featureFlag.getDescription() != null) {
                    existingFeatureFlag.setDescription(featureFlag.getDescription());
                }
                if (featureFlag.getCreatedBy() != null) {
                    existingFeatureFlag.setCreatedBy(featureFlag.getCreatedBy());
                }
                if (featureFlag.getCreatedDate() != null) {
                    existingFeatureFlag.setCreatedDate(featureFlag.getCreatedDate());
                }
                if (featureFlag.getUpdatedBy() != null) {
                    existingFeatureFlag.setUpdatedBy(featureFlag.getUpdatedBy());
                }
                if (featureFlag.getUpdatedDate() != null) {
                    existingFeatureFlag.setUpdatedDate(featureFlag.getUpdatedDate());
                }

                return existingFeatureFlag;
            })
            .map(featureFlagRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FeatureFlag> findOne(Long id) {
        LOG.debug("Request to get FeatureFlag : {}", id);
        return featureFlagRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete FeatureFlag : {}", id);
        featureFlagRepository.deleteById(id);
    }
}
