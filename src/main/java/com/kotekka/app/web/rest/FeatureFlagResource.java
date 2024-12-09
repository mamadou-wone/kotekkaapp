package com.kotekka.app.web.rest;

import com.kotekka.app.domain.FeatureFlag;
import com.kotekka.app.repository.FeatureFlagRepository;
import com.kotekka.app.service.FeatureFlagQueryService;
import com.kotekka.app.service.FeatureFlagService;
import com.kotekka.app.service.criteria.FeatureFlagCriteria;
import com.kotekka.app.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.kotekka.app.domain.FeatureFlag}.
 */
@RestController
@RequestMapping("/api/feature-flags")
public class FeatureFlagResource {

    private static final Logger LOG = LoggerFactory.getLogger(FeatureFlagResource.class);

    private static final String ENTITY_NAME = "featureFlag";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FeatureFlagService featureFlagService;

    private final FeatureFlagRepository featureFlagRepository;

    private final FeatureFlagQueryService featureFlagQueryService;

    public FeatureFlagResource(
        FeatureFlagService featureFlagService,
        FeatureFlagRepository featureFlagRepository,
        FeatureFlagQueryService featureFlagQueryService
    ) {
        this.featureFlagService = featureFlagService;
        this.featureFlagRepository = featureFlagRepository;
        this.featureFlagQueryService = featureFlagQueryService;
    }

    /**
     * {@code POST  /feature-flags} : Create a new featureFlag.
     *
     * @param featureFlag the featureFlag to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new featureFlag, or with status {@code 400 (Bad Request)} if the featureFlag has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<FeatureFlag> createFeatureFlag(@RequestBody FeatureFlag featureFlag) throws URISyntaxException {
        LOG.debug("REST request to save FeatureFlag : {}", featureFlag);
        if (featureFlag.getId() != null) {
            throw new BadRequestAlertException("A new featureFlag cannot already have an ID", ENTITY_NAME, "idexists");
        }
        featureFlag = featureFlagService.save(featureFlag);
        return ResponseEntity.created(new URI("/api/feature-flags/" + featureFlag.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, featureFlag.getId().toString()))
            .body(featureFlag);
    }

    /**
     * {@code PUT  /feature-flags/:id} : Updates an existing featureFlag.
     *
     * @param id the id of the featureFlag to save.
     * @param featureFlag the featureFlag to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated featureFlag,
     * or with status {@code 400 (Bad Request)} if the featureFlag is not valid,
     * or with status {@code 500 (Internal Server Error)} if the featureFlag couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FeatureFlag> updateFeatureFlag(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FeatureFlag featureFlag
    ) throws URISyntaxException {
        LOG.debug("REST request to update FeatureFlag : {}, {}", id, featureFlag);
        if (featureFlag.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, featureFlag.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!featureFlagRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        featureFlag = featureFlagService.update(featureFlag);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, featureFlag.getId().toString()))
            .body(featureFlag);
    }

    /**
     * {@code PATCH  /feature-flags/:id} : Partial updates given fields of an existing featureFlag, field will ignore if it is null
     *
     * @param id the id of the featureFlag to save.
     * @param featureFlag the featureFlag to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated featureFlag,
     * or with status {@code 400 (Bad Request)} if the featureFlag is not valid,
     * or with status {@code 404 (Not Found)} if the featureFlag is not found,
     * or with status {@code 500 (Internal Server Error)} if the featureFlag couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FeatureFlag> partialUpdateFeatureFlag(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FeatureFlag featureFlag
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update FeatureFlag partially : {}, {}", id, featureFlag);
        if (featureFlag.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, featureFlag.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!featureFlagRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FeatureFlag> result = featureFlagService.partialUpdate(featureFlag);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, featureFlag.getId().toString())
        );
    }

    /**
     * {@code GET  /feature-flags} : get all the featureFlags.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of featureFlags in body.
     */
    @GetMapping("")
    public ResponseEntity<List<FeatureFlag>> getAllFeatureFlags(
        FeatureFlagCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get FeatureFlags by criteria: {}", criteria);

        Page<FeatureFlag> page = featureFlagQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /feature-flags/count} : count all the featureFlags.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countFeatureFlags(FeatureFlagCriteria criteria) {
        LOG.debug("REST request to count FeatureFlags by criteria: {}", criteria);
        return ResponseEntity.ok().body(featureFlagQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /feature-flags/:id} : get the "id" featureFlag.
     *
     * @param id the id of the featureFlag to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the featureFlag, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FeatureFlag> getFeatureFlag(@PathVariable("id") Long id) {
        LOG.debug("REST request to get FeatureFlag : {}", id);
        Optional<FeatureFlag> featureFlag = featureFlagService.findOne(id);
        return ResponseUtil.wrapOrNotFound(featureFlag);
    }

    /**
     * {@code DELETE  /feature-flags/:id} : delete the "id" featureFlag.
     *
     * @param id the id of the featureFlag to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFeatureFlag(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete FeatureFlag : {}", id);
        featureFlagService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
