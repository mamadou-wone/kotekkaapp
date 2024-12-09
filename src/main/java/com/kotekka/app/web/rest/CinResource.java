package com.kotekka.app.web.rest;

import com.kotekka.app.domain.Cin;
import com.kotekka.app.repository.CinRepository;
import com.kotekka.app.service.CinQueryService;
import com.kotekka.app.service.CinService;
import com.kotekka.app.service.criteria.CinCriteria;
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
 * REST controller for managing {@link com.kotekka.app.domain.Cin}.
 */
@RestController
@RequestMapping("/api/cins")
public class CinResource {

    private static final Logger LOG = LoggerFactory.getLogger(CinResource.class);

    private static final String ENTITY_NAME = "cin";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CinService cinService;

    private final CinRepository cinRepository;

    private final CinQueryService cinQueryService;

    public CinResource(CinService cinService, CinRepository cinRepository, CinQueryService cinQueryService) {
        this.cinService = cinService;
        this.cinRepository = cinRepository;
        this.cinQueryService = cinQueryService;
    }

    /**
     * {@code POST  /cins} : Create a new cin.
     *
     * @param cin the cin to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cin, or with status {@code 400 (Bad Request)} if the cin has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Cin> createCin(@Valid @RequestBody Cin cin) throws URISyntaxException {
        LOG.debug("REST request to save Cin : {}", cin);
        if (cin.getId() != null) {
            throw new BadRequestAlertException("A new cin cannot already have an ID", ENTITY_NAME, "idexists");
        }
        cin = cinService.save(cin);
        return ResponseEntity.created(new URI("/api/cins/" + cin.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, cin.getId().toString()))
            .body(cin);
    }

    /**
     * {@code PUT  /cins/:id} : Updates an existing cin.
     *
     * @param id the id of the cin to save.
     * @param cin the cin to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cin,
     * or with status {@code 400 (Bad Request)} if the cin is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cin couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Cin> updateCin(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Cin cin)
        throws URISyntaxException {
        LOG.debug("REST request to update Cin : {}, {}", id, cin);
        if (cin.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cin.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cinRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        cin = cinService.update(cin);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cin.getId().toString()))
            .body(cin);
    }

    /**
     * {@code PATCH  /cins/:id} : Partial updates given fields of an existing cin, field will ignore if it is null
     *
     * @param id the id of the cin to save.
     * @param cin the cin to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cin,
     * or with status {@code 400 (Bad Request)} if the cin is not valid,
     * or with status {@code 404 (Not Found)} if the cin is not found,
     * or with status {@code 500 (Internal Server Error)} if the cin couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Cin> partialUpdateCin(@PathVariable(value = "id", required = false) final Long id, @NotNull @RequestBody Cin cin)
        throws URISyntaxException {
        LOG.debug("REST request to partial update Cin partially : {}, {}", id, cin);
        if (cin.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cin.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cinRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Cin> result = cinService.partialUpdate(cin);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cin.getId().toString())
        );
    }

    /**
     * {@code GET  /cins} : get all the cins.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cins in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Cin>> getAllCins(CinCriteria criteria, @org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get Cins by criteria: {}", criteria);

        Page<Cin> page = cinQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cins/count} : count all the cins.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCins(CinCriteria criteria) {
        LOG.debug("REST request to count Cins by criteria: {}", criteria);
        return ResponseEntity.ok().body(cinQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /cins/:id} : get the "id" cin.
     *
     * @param id the id of the cin to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cin, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Cin> getCin(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Cin : {}", id);
        Optional<Cin> cin = cinService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cin);
    }

    /**
     * {@code DELETE  /cins/:id} : delete the "id" cin.
     *
     * @param id the id of the cin to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCin(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Cin : {}", id);
        cinService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
