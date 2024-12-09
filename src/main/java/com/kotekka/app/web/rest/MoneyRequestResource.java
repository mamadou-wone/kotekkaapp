package com.kotekka.app.web.rest;

import com.kotekka.app.domain.MoneyRequest;
import com.kotekka.app.repository.MoneyRequestRepository;
import com.kotekka.app.service.MoneyRequestQueryService;
import com.kotekka.app.service.MoneyRequestService;
import com.kotekka.app.service.criteria.MoneyRequestCriteria;
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
 * REST controller for managing {@link com.kotekka.app.domain.MoneyRequest}.
 */
@RestController
@RequestMapping("/api/money-requests")
public class MoneyRequestResource {

    private static final Logger LOG = LoggerFactory.getLogger(MoneyRequestResource.class);

    private static final String ENTITY_NAME = "moneyRequest";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MoneyRequestService moneyRequestService;

    private final MoneyRequestRepository moneyRequestRepository;

    private final MoneyRequestQueryService moneyRequestQueryService;

    public MoneyRequestResource(
        MoneyRequestService moneyRequestService,
        MoneyRequestRepository moneyRequestRepository,
        MoneyRequestQueryService moneyRequestQueryService
    ) {
        this.moneyRequestService = moneyRequestService;
        this.moneyRequestRepository = moneyRequestRepository;
        this.moneyRequestQueryService = moneyRequestQueryService;
    }

    /**
     * {@code POST  /money-requests} : Create a new moneyRequest.
     *
     * @param moneyRequest the moneyRequest to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new moneyRequest, or with status {@code 400 (Bad Request)} if the moneyRequest has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MoneyRequest> createMoneyRequest(@Valid @RequestBody MoneyRequest moneyRequest) throws URISyntaxException {
        LOG.debug("REST request to save MoneyRequest : {}", moneyRequest);
        if (moneyRequest.getId() != null) {
            throw new BadRequestAlertException("A new moneyRequest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        moneyRequest = moneyRequestService.save(moneyRequest);
        return ResponseEntity.created(new URI("/api/money-requests/" + moneyRequest.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, moneyRequest.getId().toString()))
            .body(moneyRequest);
    }

    /**
     * {@code PUT  /money-requests/:id} : Updates an existing moneyRequest.
     *
     * @param id the id of the moneyRequest to save.
     * @param moneyRequest the moneyRequest to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated moneyRequest,
     * or with status {@code 400 (Bad Request)} if the moneyRequest is not valid,
     * or with status {@code 500 (Internal Server Error)} if the moneyRequest couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MoneyRequest> updateMoneyRequest(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MoneyRequest moneyRequest
    ) throws URISyntaxException {
        LOG.debug("REST request to update MoneyRequest : {}, {}", id, moneyRequest);
        if (moneyRequest.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, moneyRequest.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!moneyRequestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        moneyRequest = moneyRequestService.update(moneyRequest);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, moneyRequest.getId().toString()))
            .body(moneyRequest);
    }

    /**
     * {@code PATCH  /money-requests/:id} : Partial updates given fields of an existing moneyRequest, field will ignore if it is null
     *
     * @param id the id of the moneyRequest to save.
     * @param moneyRequest the moneyRequest to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated moneyRequest,
     * or with status {@code 400 (Bad Request)} if the moneyRequest is not valid,
     * or with status {@code 404 (Not Found)} if the moneyRequest is not found,
     * or with status {@code 500 (Internal Server Error)} if the moneyRequest couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MoneyRequest> partialUpdateMoneyRequest(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MoneyRequest moneyRequest
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update MoneyRequest partially : {}, {}", id, moneyRequest);
        if (moneyRequest.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, moneyRequest.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!moneyRequestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MoneyRequest> result = moneyRequestService.partialUpdate(moneyRequest);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, moneyRequest.getId().toString())
        );
    }

    /**
     * {@code GET  /money-requests} : get all the moneyRequests.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of moneyRequests in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MoneyRequest>> getAllMoneyRequests(
        MoneyRequestCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get MoneyRequests by criteria: {}", criteria);

        Page<MoneyRequest> page = moneyRequestQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /money-requests/count} : count all the moneyRequests.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countMoneyRequests(MoneyRequestCriteria criteria) {
        LOG.debug("REST request to count MoneyRequests by criteria: {}", criteria);
        return ResponseEntity.ok().body(moneyRequestQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /money-requests/:id} : get the "id" moneyRequest.
     *
     * @param id the id of the moneyRequest to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the moneyRequest, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MoneyRequest> getMoneyRequest(@PathVariable("id") Long id) {
        LOG.debug("REST request to get MoneyRequest : {}", id);
        Optional<MoneyRequest> moneyRequest = moneyRequestService.findOne(id);
        return ResponseUtil.wrapOrNotFound(moneyRequest);
    }

    /**
     * {@code DELETE  /money-requests/:id} : delete the "id" moneyRequest.
     *
     * @param id the id of the moneyRequest to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMoneyRequest(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete MoneyRequest : {}", id);
        moneyRequestService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
