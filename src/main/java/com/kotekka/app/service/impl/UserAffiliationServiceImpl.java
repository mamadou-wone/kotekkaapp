package com.kotekka.app.service.impl;

import com.kotekka.app.domain.UserAffiliation;
import com.kotekka.app.repository.UserAffiliationRepository;
import com.kotekka.app.service.UserAffiliationService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.kotekka.app.domain.UserAffiliation}.
 */
@Service
@Transactional
public class UserAffiliationServiceImpl implements UserAffiliationService {

    private static final Logger LOG = LoggerFactory.getLogger(UserAffiliationServiceImpl.class);

    private final UserAffiliationRepository userAffiliationRepository;

    public UserAffiliationServiceImpl(UserAffiliationRepository userAffiliationRepository) {
        this.userAffiliationRepository = userAffiliationRepository;
    }

    @Override
    public UserAffiliation save(UserAffiliation userAffiliation) {
        LOG.debug("Request to save UserAffiliation : {}", userAffiliation);
        return userAffiliationRepository.save(userAffiliation);
    }

    @Override
    public UserAffiliation update(UserAffiliation userAffiliation) {
        LOG.debug("Request to update UserAffiliation : {}", userAffiliation);
        return userAffiliationRepository.save(userAffiliation);
    }

    @Override
    public Optional<UserAffiliation> partialUpdate(UserAffiliation userAffiliation) {
        LOG.debug("Request to partially update UserAffiliation : {}", userAffiliation);

        return userAffiliationRepository
            .findById(userAffiliation.getId())
            .map(existingUserAffiliation -> {
                if (userAffiliation.getWalletHolder() != null) {
                    existingUserAffiliation.setWalletHolder(userAffiliation.getWalletHolder());
                }
                if (userAffiliation.getAffiliation() != null) {
                    existingUserAffiliation.setAffiliation(userAffiliation.getAffiliation());
                }
                if (userAffiliation.getCreatedBy() != null) {
                    existingUserAffiliation.setCreatedBy(userAffiliation.getCreatedBy());
                }
                if (userAffiliation.getCreatedDate() != null) {
                    existingUserAffiliation.setCreatedDate(userAffiliation.getCreatedDate());
                }

                return existingUserAffiliation;
            })
            .map(userAffiliationRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserAffiliation> findOne(Long id) {
        LOG.debug("Request to get UserAffiliation : {}", id);
        return userAffiliationRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete UserAffiliation : {}", id);
        userAffiliationRepository.deleteById(id);
    }
}
