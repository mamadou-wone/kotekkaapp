package com.kotekka.app.web.rest;

import com.kotekka.app.domain.ReferalInfo;
import com.kotekka.app.repository.ReferalInfoRepository;
import com.kotekka.app.service.ReferalInfoQueryService;
import com.kotekka.app.service.ReferalInfoService;
import com.kotekka.app.service.criteria.ReferalInfoCriteria;
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
 * REST controller for managing {@link com.kotekka.app.domain.ReferalInfo}.
 */
@RestController
@RequestMapping("/api/referal-infos")
public class ReferalInfoResource {

    private static final Logger LOG = LoggerFactory.getLogger(ReferalInfoResource.class);

    private static final String ENTITY_NAME = "referalInfo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReferalInfoService referalInfoService;

    private final ReferalInfoRepository referalInfoRepository;

    private final ReferalInfoQueryService referalInfoQueryService;

    public ReferalInfoResource(
        ReferalInfoService referalInfoService,
        ReferalInfoRepository referalInfoRepository,
        ReferalInfoQueryService referalInfoQueryService
    ) {
        this.referalInfoService = referalInfoService;
        this.referalInfoRepository = referalInfoRepository;
        this.referalInfoQueryService = referalInfoQueryService;
    }

    /**
     * {@code POST  /referal-infos} : Create a new referalInfo.
     *
     * @param referalInfo the referalInfo to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new referalInfo, or with status {@code 400 (Bad Request)} if the referalInfo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ReferalInfo> createReferalInfo(@Valid @RequestBody ReferalInfo referalInfo) throws URISyntaxException {
        LOG.debug("REST request to save ReferalInfo : {}", referalInfo);
        if (referalInfo.getId() != null) {
            throw new BadRequestAlertException("A new referalInfo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        referalInfo = referalInfoService.save(referalInfo);
        return ResponseEntity.created(new URI("/api/referal-infos/" + referalInfo.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, referalInfo.getId().toString()))
            .body(referalInfo);
    }

    /**
     * {@code PUT  /referal-infos/:id} : Updates an existing referalInfo.
     *
     * @param id the id of the referalInfo to save.
     * @param referalInfo the referalInfo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated referalInfo,
     * or with status {@code 400 (Bad Request)} if the referalInfo is not valid,
     * or with status {@code 500 (Internal Server Error)} if the referalInfo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ReferalInfo> updateReferalInfo(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ReferalInfo referalInfo
    ) throws URISyntaxException {
        LOG.debug("REST request to update ReferalInfo : {}, {}", id, referalInfo);
        if (referalInfo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, referalInfo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!referalInfoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        referalInfo = referalInfoService.update(referalInfo);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, referalInfo.getId().toString()))
            .body(referalInfo);
    }

    /**
     * {@code PATCH  /referal-infos/:id} : Partial updates given fields of an existing referalInfo, field will ignore if it is null
     *
     * @param id the id of the referalInfo to save.
     * @param referalInfo the referalInfo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated referalInfo,
     * or with status {@code 400 (Bad Request)} if the referalInfo is not valid,
     * or with status {@code 404 (Not Found)} if the referalInfo is not found,
     * or with status {@code 500 (Internal Server Error)} if the referalInfo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReferalInfo> partialUpdateReferalInfo(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ReferalInfo referalInfo
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ReferalInfo partially : {}, {}", id, referalInfo);
        if (referalInfo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, referalInfo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!referalInfoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReferalInfo> result = referalInfoService.partialUpdate(referalInfo);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, referalInfo.getId().toString())
        );
    }

    /**
     * {@code GET  /referal-infos} : get all the referalInfos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of referalInfos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ReferalInfo>> getAllReferalInfos(
        ReferalInfoCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ReferalInfos by criteria: {}", criteria);

        Page<ReferalInfo> page = referalInfoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /referal-infos/count} : count all the referalInfos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countReferalInfos(ReferalInfoCriteria criteria) {
        LOG.debug("REST request to count ReferalInfos by criteria: {}", criteria);
        return ResponseEntity.ok().body(referalInfoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /referal-infos/:id} : get the "id" referalInfo.
     *
     * @param id the id of the referalInfo to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the referalInfo, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReferalInfo> getReferalInfo(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ReferalInfo : {}", id);
        Optional<ReferalInfo> referalInfo = referalInfoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(referalInfo);
    }

    /**
     * {@code DELETE  /referal-infos/:id} : delete the "id" referalInfo.
     *
     * @param id the id of the referalInfo to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReferalInfo(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ReferalInfo : {}", id);
        referalInfoService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
