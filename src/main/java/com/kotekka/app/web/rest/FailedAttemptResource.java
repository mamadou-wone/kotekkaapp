package com.kotekka.app.web.rest;

import com.kotekka.app.domain.FailedAttempt;
import com.kotekka.app.repository.FailedAttemptRepository;
import com.kotekka.app.service.FailedAttemptQueryService;
import com.kotekka.app.service.FailedAttemptService;
import com.kotekka.app.service.criteria.FailedAttemptCriteria;
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
 * REST controller for managing {@link com.kotekka.app.domain.FailedAttempt}.
 */
@RestController
@RequestMapping("/api/failed-attempts")
public class FailedAttemptResource {

    private static final Logger LOG = LoggerFactory.getLogger(FailedAttemptResource.class);

    private static final String ENTITY_NAME = "failedAttempt";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FailedAttemptService failedAttemptService;

    private final FailedAttemptRepository failedAttemptRepository;

    private final FailedAttemptQueryService failedAttemptQueryService;

    public FailedAttemptResource(
        FailedAttemptService failedAttemptService,
        FailedAttemptRepository failedAttemptRepository,
        FailedAttemptQueryService failedAttemptQueryService
    ) {
        this.failedAttemptService = failedAttemptService;
        this.failedAttemptRepository = failedAttemptRepository;
        this.failedAttemptQueryService = failedAttemptQueryService;
    }

    /**
     * {@code POST  /failed-attempts} : Create a new failedAttempt.
     *
     * @param failedAttempt the failedAttempt to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new failedAttempt, or with status {@code 400 (Bad Request)} if the failedAttempt has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<FailedAttempt> createFailedAttempt(@Valid @RequestBody FailedAttempt failedAttempt) throws URISyntaxException {
        LOG.debug("REST request to save FailedAttempt : {}", failedAttempt);
        if (failedAttempt.getId() != null) {
            throw new BadRequestAlertException("A new failedAttempt cannot already have an ID", ENTITY_NAME, "idexists");
        }
        failedAttempt = failedAttemptService.save(failedAttempt);
        return ResponseEntity.created(new URI("/api/failed-attempts/" + failedAttempt.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, failedAttempt.getId().toString()))
            .body(failedAttempt);
    }

    /**
     * {@code PUT  /failed-attempts/:id} : Updates an existing failedAttempt.
     *
     * @param id the id of the failedAttempt to save.
     * @param failedAttempt the failedAttempt to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated failedAttempt,
     * or with status {@code 400 (Bad Request)} if the failedAttempt is not valid,
     * or with status {@code 500 (Internal Server Error)} if the failedAttempt couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FailedAttempt> updateFailedAttempt(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FailedAttempt failedAttempt
    ) throws URISyntaxException {
        LOG.debug("REST request to update FailedAttempt : {}, {}", id, failedAttempt);
        if (failedAttempt.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, failedAttempt.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!failedAttemptRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        failedAttempt = failedAttemptService.update(failedAttempt);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, failedAttempt.getId().toString()))
            .body(failedAttempt);
    }

    /**
     * {@code PATCH  /failed-attempts/:id} : Partial updates given fields of an existing failedAttempt, field will ignore if it is null
     *
     * @param id the id of the failedAttempt to save.
     * @param failedAttempt the failedAttempt to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated failedAttempt,
     * or with status {@code 400 (Bad Request)} if the failedAttempt is not valid,
     * or with status {@code 404 (Not Found)} if the failedAttempt is not found,
     * or with status {@code 500 (Internal Server Error)} if the failedAttempt couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FailedAttempt> partialUpdateFailedAttempt(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FailedAttempt failedAttempt
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update FailedAttempt partially : {}, {}", id, failedAttempt);
        if (failedAttempt.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, failedAttempt.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!failedAttemptRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FailedAttempt> result = failedAttemptService.partialUpdate(failedAttempt);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, failedAttempt.getId().toString())
        );
    }

    /**
     * {@code GET  /failed-attempts} : get all the failedAttempts.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of failedAttempts in body.
     */
    @GetMapping("")
    public ResponseEntity<List<FailedAttempt>> getAllFailedAttempts(
        FailedAttemptCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get FailedAttempts by criteria: {}", criteria);

        Page<FailedAttempt> page = failedAttemptQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /failed-attempts/count} : count all the failedAttempts.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countFailedAttempts(FailedAttemptCriteria criteria) {
        LOG.debug("REST request to count FailedAttempts by criteria: {}", criteria);
        return ResponseEntity.ok().body(failedAttemptQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /failed-attempts/:id} : get the "id" failedAttempt.
     *
     * @param id the id of the failedAttempt to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the failedAttempt, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FailedAttempt> getFailedAttempt(@PathVariable("id") Long id) {
        LOG.debug("REST request to get FailedAttempt : {}", id);
        Optional<FailedAttempt> failedAttempt = failedAttemptService.findOne(id);
        return ResponseUtil.wrapOrNotFound(failedAttempt);
    }

    /**
     * {@code DELETE  /failed-attempts/:id} : delete the "id" failedAttempt.
     *
     * @param id the id of the failedAttempt to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFailedAttempt(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete FailedAttempt : {}", id);
        failedAttemptService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
