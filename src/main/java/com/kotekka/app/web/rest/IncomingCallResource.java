package com.kotekka.app.web.rest;

import com.kotekka.app.domain.IncomingCall;
import com.kotekka.app.repository.IncomingCallRepository;
import com.kotekka.app.service.IncomingCallQueryService;
import com.kotekka.app.service.IncomingCallService;
import com.kotekka.app.service.criteria.IncomingCallCriteria;
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
 * REST controller for managing {@link com.kotekka.app.domain.IncomingCall}.
 */
@RestController
@RequestMapping("/api/incoming-calls")
public class IncomingCallResource {

    private static final Logger LOG = LoggerFactory.getLogger(IncomingCallResource.class);

    private static final String ENTITY_NAME = "incomingCall";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IncomingCallService incomingCallService;

    private final IncomingCallRepository incomingCallRepository;

    private final IncomingCallQueryService incomingCallQueryService;

    public IncomingCallResource(
        IncomingCallService incomingCallService,
        IncomingCallRepository incomingCallRepository,
        IncomingCallQueryService incomingCallQueryService
    ) {
        this.incomingCallService = incomingCallService;
        this.incomingCallRepository = incomingCallRepository;
        this.incomingCallQueryService = incomingCallQueryService;
    }

    /**
     * {@code POST  /incoming-calls} : Create a new incomingCall.
     *
     * @param incomingCall the incomingCall to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new incomingCall, or with status {@code 400 (Bad Request)} if the incomingCall has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<IncomingCall> createIncomingCall(@Valid @RequestBody IncomingCall incomingCall) throws URISyntaxException {
        LOG.debug("REST request to save IncomingCall : {}", incomingCall);
        if (incomingCall.getId() != null) {
            throw new BadRequestAlertException("A new incomingCall cannot already have an ID", ENTITY_NAME, "idexists");
        }
        incomingCall = incomingCallService.save(incomingCall);
        return ResponseEntity.created(new URI("/api/incoming-calls/" + incomingCall.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, incomingCall.getId().toString()))
            .body(incomingCall);
    }

    /**
     * {@code PUT  /incoming-calls/:id} : Updates an existing incomingCall.
     *
     * @param id the id of the incomingCall to save.
     * @param incomingCall the incomingCall to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated incomingCall,
     * or with status {@code 400 (Bad Request)} if the incomingCall is not valid,
     * or with status {@code 500 (Internal Server Error)} if the incomingCall couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<IncomingCall> updateIncomingCall(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody IncomingCall incomingCall
    ) throws URISyntaxException {
        LOG.debug("REST request to update IncomingCall : {}, {}", id, incomingCall);
        if (incomingCall.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, incomingCall.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!incomingCallRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        incomingCall = incomingCallService.update(incomingCall);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, incomingCall.getId().toString()))
            .body(incomingCall);
    }

    /**
     * {@code PATCH  /incoming-calls/:id} : Partial updates given fields of an existing incomingCall, field will ignore if it is null
     *
     * @param id the id of the incomingCall to save.
     * @param incomingCall the incomingCall to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated incomingCall,
     * or with status {@code 400 (Bad Request)} if the incomingCall is not valid,
     * or with status {@code 404 (Not Found)} if the incomingCall is not found,
     * or with status {@code 500 (Internal Server Error)} if the incomingCall couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<IncomingCall> partialUpdateIncomingCall(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody IncomingCall incomingCall
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update IncomingCall partially : {}, {}", id, incomingCall);
        if (incomingCall.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, incomingCall.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!incomingCallRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<IncomingCall> result = incomingCallService.partialUpdate(incomingCall);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, incomingCall.getId().toString())
        );
    }

    /**
     * {@code GET  /incoming-calls} : get all the incomingCalls.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of incomingCalls in body.
     */
    @GetMapping("")
    public ResponseEntity<List<IncomingCall>> getAllIncomingCalls(
        IncomingCallCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get IncomingCalls by criteria: {}", criteria);

        Page<IncomingCall> page = incomingCallQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /incoming-calls/count} : count all the incomingCalls.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countIncomingCalls(IncomingCallCriteria criteria) {
        LOG.debug("REST request to count IncomingCalls by criteria: {}", criteria);
        return ResponseEntity.ok().body(incomingCallQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /incoming-calls/:id} : get the "id" incomingCall.
     *
     * @param id the id of the incomingCall to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the incomingCall, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<IncomingCall> getIncomingCall(@PathVariable("id") Long id) {
        LOG.debug("REST request to get IncomingCall : {}", id);
        Optional<IncomingCall> incomingCall = incomingCallService.findOne(id);
        return ResponseUtil.wrapOrNotFound(incomingCall);
    }

    /**
     * {@code DELETE  /incoming-calls/:id} : delete the "id" incomingCall.
     *
     * @param id the id of the incomingCall to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIncomingCall(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete IncomingCall : {}", id);
        incomingCallService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
