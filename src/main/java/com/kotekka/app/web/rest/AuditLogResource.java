package com.kotekka.app.web.rest;

import com.kotekka.app.domain.AuditLog;
import com.kotekka.app.repository.AuditLogRepository;
import com.kotekka.app.service.AuditLogQueryService;
import com.kotekka.app.service.AuditLogService;
import com.kotekka.app.service.criteria.AuditLogCriteria;
import com.kotekka.app.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.kotekka.app.domain.AuditLog}.
 */
@RestController
@RequestMapping("/api/audit-logs")
public class AuditLogResource {

    private static final Logger LOG = LoggerFactory.getLogger(AuditLogResource.class);

    private static final String ENTITY_NAME = "auditLog";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AuditLogService auditLogService;

    private final AuditLogRepository auditLogRepository;

    private final AuditLogQueryService auditLogQueryService;

    public AuditLogResource(
        AuditLogService auditLogService,
        AuditLogRepository auditLogRepository,
        AuditLogQueryService auditLogQueryService
    ) {
        this.auditLogService = auditLogService;
        this.auditLogRepository = auditLogRepository;
        this.auditLogQueryService = auditLogQueryService;
    }

    /**
     * {@code POST  /audit-logs} : Create a new auditLog.
     *
     * @param auditLog the auditLog to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new auditLog, or with status {@code 400 (Bad Request)} if the auditLog has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AuditLog> createAuditLog(@Valid @RequestBody AuditLog auditLog) throws URISyntaxException {
        LOG.debug("REST request to save AuditLog : {}", auditLog);
        if (auditLog.getId() != null) {
            throw new BadRequestAlertException("A new auditLog cannot already have an ID", ENTITY_NAME, "idexists");
        }
        auditLog = auditLogService.save(auditLog);
        return ResponseEntity.created(new URI("/api/audit-logs/" + auditLog.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, auditLog.getId().toString()))
            .body(auditLog);
    }

    /**
     * {@code PUT  /audit-logs/:id} : Updates an existing auditLog.
     *
     * @param id the id of the auditLog to save.
     * @param auditLog the auditLog to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated auditLog,
     * or with status {@code 400 (Bad Request)} if the auditLog is not valid,
     * or with status {@code 500 (Internal Server Error)} if the auditLog couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AuditLog> updateAuditLog(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AuditLog auditLog
    ) throws URISyntaxException {
        LOG.debug("REST request to update AuditLog : {}, {}", id, auditLog);
        if (auditLog.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, auditLog.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!auditLogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        auditLog = auditLogService.update(auditLog);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, auditLog.getId().toString()))
            .body(auditLog);
    }

    /**
     * {@code PATCH  /audit-logs/:id} : Partial updates given fields of an existing auditLog, field will ignore if it is null
     *
     * @param id the id of the auditLog to save.
     * @param auditLog the auditLog to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated auditLog,
     * or with status {@code 400 (Bad Request)} if the auditLog is not valid,
     * or with status {@code 404 (Not Found)} if the auditLog is not found,
     * or with status {@code 500 (Internal Server Error)} if the auditLog couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AuditLog> partialUpdateAuditLog(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AuditLog auditLog
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AuditLog partially : {}, {}", id, auditLog);
        if (auditLog.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, auditLog.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!auditLogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AuditLog> result = auditLogService.partialUpdate(auditLog);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, auditLog.getId().toString())
        );
    }

    /**
     * {@code GET  /audit-logs} : get all the auditLogs.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of auditLogs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AuditLog>> getAllAuditLogs(
        AuditLogCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AuditLogs by criteria: {}", criteria);

        Page<AuditLog> page = auditLogQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /audit-logs/count} : count all the auditLogs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAuditLogs(AuditLogCriteria criteria) {
        LOG.debug("REST request to count AuditLogs by criteria: {}", criteria);
        return ResponseEntity.ok().body(auditLogQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /audit-logs/:id} : get the "id" auditLog.
     *
     * @param id the id of the auditLog to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the auditLog, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AuditLog> getAuditLog(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AuditLog : {}", id);
        Optional<AuditLog> auditLog = auditLogService.findOne(id);
        return ResponseUtil.wrapOrNotFound(auditLog);
    }

    /**
     * {@code DELETE  /audit-logs/:id} : delete the "id" auditLog.
     *
     * @param id the id of the auditLog to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuditLog(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AuditLog : {}", id);
        auditLogService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
