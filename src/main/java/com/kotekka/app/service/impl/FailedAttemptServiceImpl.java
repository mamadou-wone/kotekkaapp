package com.kotekka.app.service.impl;

import com.kotekka.app.domain.FailedAttempt;
import com.kotekka.app.repository.FailedAttemptRepository;
import com.kotekka.app.service.FailedAttemptService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.kotekka.app.domain.FailedAttempt}.
 */
@Service
@Transactional
public class FailedAttemptServiceImpl implements FailedAttemptService {

    private static final Logger LOG = LoggerFactory.getLogger(FailedAttemptServiceImpl.class);

    private final FailedAttemptRepository failedAttemptRepository;

    public FailedAttemptServiceImpl(FailedAttemptRepository failedAttemptRepository) {
        this.failedAttemptRepository = failedAttemptRepository;
    }

    @Override
    public FailedAttempt save(FailedAttempt failedAttempt) {
        LOG.debug("Request to save FailedAttempt : {}", failedAttempt);
        return failedAttemptRepository.save(failedAttempt);
    }

    @Override
    public FailedAttempt update(FailedAttempt failedAttempt) {
        LOG.debug("Request to update FailedAttempt : {}", failedAttempt);
        return failedAttemptRepository.save(failedAttempt);
    }

    @Override
    public Optional<FailedAttempt> partialUpdate(FailedAttempt failedAttempt) {
        LOG.debug("Request to partially update FailedAttempt : {}", failedAttempt);

        return failedAttemptRepository
            .findById(failedAttempt.getId())
            .map(existingFailedAttempt -> {
                if (failedAttempt.getLogin() != null) {
                    existingFailedAttempt.setLogin(failedAttempt.getLogin());
                }
                if (failedAttempt.getIpAddress() != null) {
                    existingFailedAttempt.setIpAddress(failedAttempt.getIpAddress());
                }
                if (failedAttempt.getIsAfterLock() != null) {
                    existingFailedAttempt.setIsAfterLock(failedAttempt.getIsAfterLock());
                }
                if (failedAttempt.getApp() != null) {
                    existingFailedAttempt.setApp(failedAttempt.getApp());
                }
                if (failedAttempt.getAction() != null) {
                    existingFailedAttempt.setAction(failedAttempt.getAction());
                }
                if (failedAttempt.getDevice() != null) {
                    existingFailedAttempt.setDevice(failedAttempt.getDevice());
                }
                if (failedAttempt.getCreatedDate() != null) {
                    existingFailedAttempt.setCreatedDate(failedAttempt.getCreatedDate());
                }
                if (failedAttempt.getReason() != null) {
                    existingFailedAttempt.setReason(failedAttempt.getReason());
                }

                return existingFailedAttempt;
            })
            .map(failedAttemptRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FailedAttempt> findOne(Long id) {
        LOG.debug("Request to get FailedAttempt : {}", id);
        return failedAttemptRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete FailedAttempt : {}", id);
        failedAttemptRepository.deleteById(id);
    }
}
