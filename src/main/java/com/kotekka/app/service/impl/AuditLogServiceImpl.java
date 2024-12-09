package com.kotekka.app.service.impl;

import com.kotekka.app.domain.AuditLog;
import com.kotekka.app.repository.AuditLogRepository;
import com.kotekka.app.service.AuditLogService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.kotekka.app.domain.AuditLog}.
 */
@Service
@Transactional
public class AuditLogServiceImpl implements AuditLogService {

    private static final Logger LOG = LoggerFactory.getLogger(AuditLogServiceImpl.class);

    private final AuditLogRepository auditLogRepository;

    public AuditLogServiceImpl(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Override
    public AuditLog save(AuditLog auditLog) {
        LOG.debug("Request to save AuditLog : {}", auditLog);
        return auditLogRepository.save(auditLog);
    }

    @Override
    public AuditLog update(AuditLog auditLog) {
        LOG.debug("Request to update AuditLog : {}", auditLog);
        return auditLogRepository.save(auditLog);
    }

    @Override
    public Optional<AuditLog> partialUpdate(AuditLog auditLog) {
        LOG.debug("Request to partially update AuditLog : {}", auditLog);

        return auditLogRepository
            .findById(auditLog.getId())
            .map(existingAuditLog -> {
                if (auditLog.getUuid() != null) {
                    existingAuditLog.setUuid(auditLog.getUuid());
                }
                if (auditLog.getEntityName() != null) {
                    existingAuditLog.setEntityName(auditLog.getEntityName());
                }
                if (auditLog.getEntityId() != null) {
                    existingAuditLog.setEntityId(auditLog.getEntityId());
                }
                if (auditLog.getAction() != null) {
                    existingAuditLog.setAction(auditLog.getAction());
                }
                if (auditLog.getPerformedBy() != null) {
                    existingAuditLog.setPerformedBy(auditLog.getPerformedBy());
                }
                if (auditLog.getPerformedDate() != null) {
                    existingAuditLog.setPerformedDate(auditLog.getPerformedDate());
                }
                if (auditLog.getDetails() != null) {
                    existingAuditLog.setDetails(auditLog.getDetails());
                }

                return existingAuditLog;
            })
            .map(auditLogRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AuditLog> findOne(Long id) {
        LOG.debug("Request to get AuditLog : {}", id);
        return auditLogRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete AuditLog : {}", id);
        auditLogRepository.deleteById(id);
    }
}
