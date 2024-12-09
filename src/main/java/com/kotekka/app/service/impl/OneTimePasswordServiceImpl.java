package com.kotekka.app.service.impl;

import com.kotekka.app.domain.OneTimePassword;
import com.kotekka.app.repository.OneTimePasswordRepository;
import com.kotekka.app.service.OneTimePasswordService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.kotekka.app.domain.OneTimePassword}.
 */
@Service
@Transactional
public class OneTimePasswordServiceImpl implements OneTimePasswordService {

    private static final Logger LOG = LoggerFactory.getLogger(OneTimePasswordServiceImpl.class);

    private final OneTimePasswordRepository oneTimePasswordRepository;

    public OneTimePasswordServiceImpl(OneTimePasswordRepository oneTimePasswordRepository) {
        this.oneTimePasswordRepository = oneTimePasswordRepository;
    }

    @Override
    public OneTimePassword save(OneTimePassword oneTimePassword) {
        LOG.debug("Request to save OneTimePassword : {}", oneTimePassword);
        return oneTimePasswordRepository.save(oneTimePassword);
    }

    @Override
    public OneTimePassword update(OneTimePassword oneTimePassword) {
        LOG.debug("Request to update OneTimePassword : {}", oneTimePassword);
        return oneTimePasswordRepository.save(oneTimePassword);
    }

    @Override
    public Optional<OneTimePassword> partialUpdate(OneTimePassword oneTimePassword) {
        LOG.debug("Request to partially update OneTimePassword : {}", oneTimePassword);

        return oneTimePasswordRepository
            .findById(oneTimePassword.getId())
            .map(existingOneTimePassword -> {
                if (oneTimePassword.getUuid() != null) {
                    existingOneTimePassword.setUuid(oneTimePassword.getUuid());
                }
                if (oneTimePassword.getUser() != null) {
                    existingOneTimePassword.setUser(oneTimePassword.getUser());
                }
                if (oneTimePassword.getCode() != null) {
                    existingOneTimePassword.setCode(oneTimePassword.getCode());
                }
                if (oneTimePassword.getStatus() != null) {
                    existingOneTimePassword.setStatus(oneTimePassword.getStatus());
                }
                if (oneTimePassword.getExpiry() != null) {
                    existingOneTimePassword.setExpiry(oneTimePassword.getExpiry());
                }
                if (oneTimePassword.getCreatedDate() != null) {
                    existingOneTimePassword.setCreatedDate(oneTimePassword.getCreatedDate());
                }

                return existingOneTimePassword;
            })
            .map(oneTimePasswordRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OneTimePassword> findOne(Long id) {
        LOG.debug("Request to get OneTimePassword : {}", id);
        return oneTimePasswordRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete OneTimePassword : {}", id);
        oneTimePasswordRepository.deleteById(id);
    }
}
