package com.kotekka.app.web.rest;

import com.kotekka.app.domain.FailedAttemptHistory;
import com.kotekka.app.repository.FailedAttemptHistoryRepository;
import com.kotekka.app.service.FailedAttemptHistoryService;
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
 * REST controller for managing {@link com.kotekka.app.domain.FailedAttemptHistory}.
 */
@RestController
@RequestMapping("/api/failed-attempt-histories")
public class FailedAttemptHistoryResource {

    private static final Logger LOG = LoggerFactory.getLogger(FailedAttemptHistoryResource.class);

    private static final String ENTITY_NAME = "failedAttemptHistory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FailedAttemptHistoryService failedAttemptHistoryService;

    private final FailedAttemptHistoryRepository failedAttemptHistoryRepository;

    public FailedAttemptHistoryResource(
        FailedAttemptHistoryService failedAttemptHistoryService,
        FailedAttemptHistoryRepository failedAttemptHistoryRepository
    ) {
        this.failedAttemptHistoryService = failedAttemptHistoryService;
        this.failedAttemptHistoryRepository = failedAttemptHistoryRepository;
    }

    /**
     * {@code POST  /failed-attempt-histories} : Create a new failedAttemptHistory.
     *
     * @param failedAttemptHistory the failedAttemptHistory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new failedAttemptHistory, or with status {@code 400 (Bad Request)} if the failedAttemptHistory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<FailedAttemptHistory> createFailedAttemptHistory(@Valid @RequestBody FailedAttemptHistory failedAttemptHistory)
        throws URISyntaxException {
        LOG.debug("REST request to save FailedAttemptHistory : {}", failedAttemptHistory);
        if (failedAttemptHistory.getId() != null) {
            throw new BadRequestAlertException("A new failedAttemptHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        failedAttemptHistory = failedAttemptHistoryService.save(failedAttemptHistory);
        return ResponseEntity.created(new URI("/api/failed-attempt-histories/" + failedAttemptHistory.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, failedAttemptHistory.getId().toString()))
            .body(failedAttemptHistory);
    }

    /**
     * {@code PUT  /failed-attempt-histories/:id} : Updates an existing failedAttemptHistory.
     *
     * @param id the id of the failedAttemptHistory to save.
     * @param failedAttemptHistory the failedAttemptHistory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated failedAttemptHistory,
     * or with status {@code 400 (Bad Request)} if the failedAttemptHistory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the failedAttemptHistory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FailedAttemptHistory> updateFailedAttemptHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FailedAttemptHistory failedAttemptHistory
    ) throws URISyntaxException {
        LOG.debug("REST request to update FailedAttemptHistory : {}, {}", id, failedAttemptHistory);
        if (failedAttemptHistory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, failedAttemptHistory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!failedAttemptHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        failedAttemptHistory = failedAttemptHistoryService.update(failedAttemptHistory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, failedAttemptHistory.getId().toString()))
            .body(failedAttemptHistory);
    }

    /**
     * {@code PATCH  /failed-attempt-histories/:id} : Partial updates given fields of an existing failedAttemptHistory, field will ignore if it is null
     *
     * @param id the id of the failedAttemptHistory to save.
     * @param failedAttemptHistory the failedAttemptHistory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated failedAttemptHistory,
     * or with status {@code 400 (Bad Request)} if the failedAttemptHistory is not valid,
     * or with status {@code 404 (Not Found)} if the failedAttemptHistory is not found,
     * or with status {@code 500 (Internal Server Error)} if the failedAttemptHistory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FailedAttemptHistory> partialUpdateFailedAttemptHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FailedAttemptHistory failedAttemptHistory
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update FailedAttemptHistory partially : {}, {}", id, failedAttemptHistory);
        if (failedAttemptHistory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, failedAttemptHistory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!failedAttemptHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FailedAttemptHistory> result = failedAttemptHistoryService.partialUpdate(failedAttemptHistory);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, failedAttemptHistory.getId().toString())
        );
    }

    /**
     * {@code GET  /failed-attempt-histories} : get all the failedAttemptHistories.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of failedAttemptHistories in body.
     */
    @GetMapping("")
    public ResponseEntity<List<FailedAttemptHistory>> getAllFailedAttemptHistories(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of FailedAttemptHistories");
        Page<FailedAttemptHistory> page = failedAttemptHistoryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /failed-attempt-histories/:id} : get the "id" failedAttemptHistory.
     *
     * @param id the id of the failedAttemptHistory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the failedAttemptHistory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FailedAttemptHistory> getFailedAttemptHistory(@PathVariable("id") Long id) {
        LOG.debug("REST request to get FailedAttemptHistory : {}", id);
        Optional<FailedAttemptHistory> failedAttemptHistory = failedAttemptHistoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(failedAttemptHistory);
    }

    /**
     * {@code DELETE  /failed-attempt-histories/:id} : delete the "id" failedAttemptHistory.
     *
     * @param id the id of the failedAttemptHistory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFailedAttemptHistory(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete FailedAttemptHistory : {}", id);
        failedAttemptHistoryService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
