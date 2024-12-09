package com.kotekka.app.web.rest;

import com.kotekka.app.domain.PartnerCall;
import com.kotekka.app.repository.PartnerCallRepository;
import com.kotekka.app.service.PartnerCallQueryService;
import com.kotekka.app.service.PartnerCallService;
import com.kotekka.app.service.criteria.PartnerCallCriteria;
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
 * REST controller for managing {@link com.kotekka.app.domain.PartnerCall}.
 */
@RestController
@RequestMapping("/api/partner-calls")
public class PartnerCallResource {

    private static final Logger LOG = LoggerFactory.getLogger(PartnerCallResource.class);

    private static final String ENTITY_NAME = "partnerCall";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PartnerCallService partnerCallService;

    private final PartnerCallRepository partnerCallRepository;

    private final PartnerCallQueryService partnerCallQueryService;

    public PartnerCallResource(
        PartnerCallService partnerCallService,
        PartnerCallRepository partnerCallRepository,
        PartnerCallQueryService partnerCallQueryService
    ) {
        this.partnerCallService = partnerCallService;
        this.partnerCallRepository = partnerCallRepository;
        this.partnerCallQueryService = partnerCallQueryService;
    }

    /**
     * {@code POST  /partner-calls} : Create a new partnerCall.
     *
     * @param partnerCall the partnerCall to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new partnerCall, or with status {@code 400 (Bad Request)} if the partnerCall has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PartnerCall> createPartnerCall(@Valid @RequestBody PartnerCall partnerCall) throws URISyntaxException {
        LOG.debug("REST request to save PartnerCall : {}", partnerCall);
        if (partnerCall.getId() != null) {
            throw new BadRequestAlertException("A new partnerCall cannot already have an ID", ENTITY_NAME, "idexists");
        }
        partnerCall = partnerCallService.save(partnerCall);
        return ResponseEntity.created(new URI("/api/partner-calls/" + partnerCall.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, partnerCall.getId().toString()))
            .body(partnerCall);
    }

    /**
     * {@code PUT  /partner-calls/:id} : Updates an existing partnerCall.
     *
     * @param id the id of the partnerCall to save.
     * @param partnerCall the partnerCall to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated partnerCall,
     * or with status {@code 400 (Bad Request)} if the partnerCall is not valid,
     * or with status {@code 500 (Internal Server Error)} if the partnerCall couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PartnerCall> updatePartnerCall(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PartnerCall partnerCall
    ) throws URISyntaxException {
        LOG.debug("REST request to update PartnerCall : {}, {}", id, partnerCall);
        if (partnerCall.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, partnerCall.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!partnerCallRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        partnerCall = partnerCallService.update(partnerCall);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, partnerCall.getId().toString()))
            .body(partnerCall);
    }

    /**
     * {@code PATCH  /partner-calls/:id} : Partial updates given fields of an existing partnerCall, field will ignore if it is null
     *
     * @param id the id of the partnerCall to save.
     * @param partnerCall the partnerCall to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated partnerCall,
     * or with status {@code 400 (Bad Request)} if the partnerCall is not valid,
     * or with status {@code 404 (Not Found)} if the partnerCall is not found,
     * or with status {@code 500 (Internal Server Error)} if the partnerCall couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PartnerCall> partialUpdatePartnerCall(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PartnerCall partnerCall
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PartnerCall partially : {}, {}", id, partnerCall);
        if (partnerCall.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, partnerCall.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!partnerCallRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PartnerCall> result = partnerCallService.partialUpdate(partnerCall);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, partnerCall.getId().toString())
        );
    }

    /**
     * {@code GET  /partner-calls} : get all the partnerCalls.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of partnerCalls in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PartnerCall>> getAllPartnerCalls(
        PartnerCallCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get PartnerCalls by criteria: {}", criteria);

        Page<PartnerCall> page = partnerCallQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /partner-calls/count} : count all the partnerCalls.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countPartnerCalls(PartnerCallCriteria criteria) {
        LOG.debug("REST request to count PartnerCalls by criteria: {}", criteria);
        return ResponseEntity.ok().body(partnerCallQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /partner-calls/:id} : get the "id" partnerCall.
     *
     * @param id the id of the partnerCall to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the partnerCall, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PartnerCall> getPartnerCall(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PartnerCall : {}", id);
        Optional<PartnerCall> partnerCall = partnerCallService.findOne(id);
        return ResponseUtil.wrapOrNotFound(partnerCall);
    }

    /**
     * {@code DELETE  /partner-calls/:id} : delete the "id" partnerCall.
     *
     * @param id the id of the partnerCall to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePartnerCall(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PartnerCall : {}", id);
        partnerCallService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
