package com.kotekka.app.service;

import com.kotekka.app.domain.AuditLog;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.kotekka.app.domain.AuditLog}.
 */
public interface AuditLogService {
    /**
     * Save a auditLog.
     *
     * @param auditLog the entity to save.
     * @return the persisted entity.
     */
    AuditLog save(AuditLog auditLog);

    /**
     * Updates a auditLog.
     *
     * @param auditLog the entity to update.
     * @return the persisted entity.
     */
    AuditLog update(AuditLog auditLog);

    /**
     * Partially updates a auditLog.
     *
     * @param auditLog the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AuditLog> partialUpdate(AuditLog auditLog);

    /**
     * Get the "id" auditLog.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AuditLog> findOne(Long id);

    /**
     * Delete the "id" auditLog.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
