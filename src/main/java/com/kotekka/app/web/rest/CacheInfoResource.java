package com.kotekka.app.web.rest;

import com.kotekka.app.domain.CacheInfo;
import com.kotekka.app.repository.CacheInfoRepository;
import com.kotekka.app.service.CacheInfoQueryService;
import com.kotekka.app.service.CacheInfoService;
import com.kotekka.app.service.criteria.CacheInfoCriteria;
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
 * REST controller for managing {@link com.kotekka.app.domain.CacheInfo}.
 */
@RestController
@RequestMapping("/api/cache-infos")
public class CacheInfoResource {

    private static final Logger LOG = LoggerFactory.getLogger(CacheInfoResource.class);

    private static final String ENTITY_NAME = "cacheInfo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CacheInfoService cacheInfoService;

    private final CacheInfoRepository cacheInfoRepository;

    private final CacheInfoQueryService cacheInfoQueryService;

    public CacheInfoResource(
        CacheInfoService cacheInfoService,
        CacheInfoRepository cacheInfoRepository,
        CacheInfoQueryService cacheInfoQueryService
    ) {
        this.cacheInfoService = cacheInfoService;
        this.cacheInfoRepository = cacheInfoRepository;
        this.cacheInfoQueryService = cacheInfoQueryService;
    }

    /**
     * {@code POST  /cache-infos} : Create a new cacheInfo.
     *
     * @param cacheInfo the cacheInfo to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cacheInfo, or with status {@code 400 (Bad Request)} if the cacheInfo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CacheInfo> createCacheInfo(@Valid @RequestBody CacheInfo cacheInfo) throws URISyntaxException {
        LOG.debug("REST request to save CacheInfo : {}", cacheInfo);
        if (cacheInfo.getId() != null) {
            throw new BadRequestAlertException("A new cacheInfo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        cacheInfo = cacheInfoService.save(cacheInfo);
        return ResponseEntity.created(new URI("/api/cache-infos/" + cacheInfo.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, cacheInfo.getId().toString()))
            .body(cacheInfo);
    }

    /**
     * {@code PUT  /cache-infos/:id} : Updates an existing cacheInfo.
     *
     * @param id the id of the cacheInfo to save.
     * @param cacheInfo the cacheInfo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cacheInfo,
     * or with status {@code 400 (Bad Request)} if the cacheInfo is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cacheInfo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CacheInfo> updateCacheInfo(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CacheInfo cacheInfo
    ) throws URISyntaxException {
        LOG.debug("REST request to update CacheInfo : {}, {}", id, cacheInfo);
        if (cacheInfo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cacheInfo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cacheInfoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        cacheInfo = cacheInfoService.update(cacheInfo);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cacheInfo.getId().toString()))
            .body(cacheInfo);
    }

    /**
     * {@code PATCH  /cache-infos/:id} : Partial updates given fields of an existing cacheInfo, field will ignore if it is null
     *
     * @param id the id of the cacheInfo to save.
     * @param cacheInfo the cacheInfo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cacheInfo,
     * or with status {@code 400 (Bad Request)} if the cacheInfo is not valid,
     * or with status {@code 404 (Not Found)} if the cacheInfo is not found,
     * or with status {@code 500 (Internal Server Error)} if the cacheInfo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CacheInfo> partialUpdateCacheInfo(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CacheInfo cacheInfo
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CacheInfo partially : {}, {}", id, cacheInfo);
        if (cacheInfo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cacheInfo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cacheInfoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CacheInfo> result = cacheInfoService.partialUpdate(cacheInfo);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cacheInfo.getId().toString())
        );
    }

    /**
     * {@code GET  /cache-infos} : get all the cacheInfos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cacheInfos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CacheInfo>> getAllCacheInfos(
        CacheInfoCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get CacheInfos by criteria: {}", criteria);

        Page<CacheInfo> page = cacheInfoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cache-infos/count} : count all the cacheInfos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCacheInfos(CacheInfoCriteria criteria) {
        LOG.debug("REST request to count CacheInfos by criteria: {}", criteria);
        return ResponseEntity.ok().body(cacheInfoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /cache-infos/:id} : get the "id" cacheInfo.
     *
     * @param id the id of the cacheInfo to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cacheInfo, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CacheInfo> getCacheInfo(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CacheInfo : {}", id);
        Optional<CacheInfo> cacheInfo = cacheInfoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cacheInfo);
    }

    /**
     * {@code DELETE  /cache-infos/:id} : delete the "id" cacheInfo.
     *
     * @param id the id of the cacheInfo to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCacheInfo(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CacheInfo : {}", id);
        cacheInfoService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
