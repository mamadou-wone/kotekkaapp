package com.kotekka.app.service.impl;

import com.kotekka.app.domain.FailedAttemptHistory;
import com.kotekka.app.repository.FailedAttemptHistoryRepository;
import com.kotekka.app.service.FailedAttemptHistoryService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.kotekka.app.domain.FailedAttemptHistory}.
 */
@Service
@Transactional
public class FailedAttemptHistoryServiceImpl implements FailedAttemptHistoryService {

    private static final Logger LOG = LoggerFactory.getLogger(FailedAttemptHistoryServiceImpl.class);

    private final FailedAttemptHistoryRepository failedAttemptHistoryRepository;

    public FailedAttemptHistoryServiceImpl(FailedAttemptHistoryRepository failedAttemptHistoryRepository) {
        this.failedAttemptHistoryRepository = failedAttemptHistoryRepository;
    }

    @Override
    public FailedAttemptHistory save(FailedAttemptHistory failedAttemptHistory) {
        LOG.debug("Request to save FailedAttemptHistory : {}", failedAttemptHistory);
        return failedAttemptHistoryRepository.save(failedAttemptHistory);
    }

    @Override
    public FailedAttemptHistory update(FailedAttemptHistory failedAttemptHistory) {
        LOG.debug("Request to update FailedAttemptHistory : {}", failedAttemptHistory);
        return failedAttemptHistoryRepository.save(failedAttemptHistory);
    }

    @Override
    public Optional<FailedAttemptHistory> partialUpdate(FailedAttemptHistory failedAttemptHistory) {
        LOG.debug("Request to partially update FailedAttemptHistory : {}", failedAttemptHistory);

        return failedAttemptHistoryRepository
            .findById(failedAttemptHistory.getId())
            .map(existingFailedAttemptHistory -> {
                if (failedAttemptHistory.getLogin() != null) {
                    existingFailedAttemptHistory.setLogin(failedAttemptHistory.getLogin());
                }
                if (failedAttemptHistory.getIpAddress() != null) {
                    existingFailedAttemptHistory.setIpAddress(failedAttemptHistory.getIpAddress());
                }
                if (failedAttemptHistory.getIsAfterLock() != null) {
                    existingFailedAttemptHistory.setIsAfterLock(failedAttemptHistory.getIsAfterLock());
                }
                if (failedAttemptHistory.getApp() != null) {
                    existingFailedAttemptHistory.setApp(failedAttemptHistory.getApp());
                }
                if (failedAttemptHistory.getAction() != null) {
                    existingFailedAttemptHistory.setAction(failedAttemptHistory.getAction());
                }
                if (failedAttemptHistory.getDevice() != null) {
                    existingFailedAttemptHistory.setDevice(failedAttemptHistory.getDevice());
                }
                if (failedAttemptHistory.getCreatedDate() != null) {
                    existingFailedAttemptHistory.setCreatedDate(failedAttemptHistory.getCreatedDate());
                }
                if (failedAttemptHistory.getReason() != null) {
                    existingFailedAttemptHistory.setReason(failedAttemptHistory.getReason());
                }

                return existingFailedAttemptHistory;
            })
            .map(failedAttemptHistoryRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FailedAttemptHistory> findAll(Pageable pageable) {
        LOG.debug("Request to get all FailedAttemptHistories");
        return failedAttemptHistoryRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FailedAttemptHistory> findOne(Long id) {
        LOG.debug("Request to get FailedAttemptHistory : {}", id);
        return failedAttemptHistoryRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete FailedAttemptHistory : {}", id);
        failedAttemptHistoryRepository.deleteById(id);
    }
}
