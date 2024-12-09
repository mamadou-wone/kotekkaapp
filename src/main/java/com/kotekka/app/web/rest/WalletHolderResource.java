package com.kotekka.app.web.rest;

import com.kotekka.app.domain.WalletHolder;
import com.kotekka.app.repository.WalletHolderRepository;
import com.kotekka.app.service.WalletHolderQueryService;
import com.kotekka.app.service.WalletHolderService;
import com.kotekka.app.service.criteria.WalletHolderCriteria;
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
 * REST controller for managing {@link com.kotekka.app.domain.WalletHolder}.
 */
@RestController
@RequestMapping("/api/wallet-holders")
public class WalletHolderResource {

    private static final Logger LOG = LoggerFactory.getLogger(WalletHolderResource.class);

    private static final String ENTITY_NAME = "walletHolder";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WalletHolderService walletHolderService;

    private final WalletHolderRepository walletHolderRepository;

    private final WalletHolderQueryService walletHolderQueryService;

    public WalletHolderResource(
        WalletHolderService walletHolderService,
        WalletHolderRepository walletHolderRepository,
        WalletHolderQueryService walletHolderQueryService
    ) {
        this.walletHolderService = walletHolderService;
        this.walletHolderRepository = walletHolderRepository;
        this.walletHolderQueryService = walletHolderQueryService;
    }

    /**
     * {@code POST  /wallet-holders} : Create a new walletHolder.
     *
     * @param walletHolder the walletHolder to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new walletHolder, or with status {@code 400 (Bad Request)} if the walletHolder has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<WalletHolder> createWalletHolder(@Valid @RequestBody WalletHolder walletHolder) throws URISyntaxException {
        LOG.debug("REST request to save WalletHolder : {}", walletHolder);
        if (walletHolder.getId() != null) {
            throw new BadRequestAlertException("A new walletHolder cannot already have an ID", ENTITY_NAME, "idexists");
        }
        walletHolder = walletHolderService.save(walletHolder);
        return ResponseEntity.created(new URI("/api/wallet-holders/" + walletHolder.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, walletHolder.getId().toString()))
            .body(walletHolder);
    }

    /**
     * {@code PUT  /wallet-holders/:id} : Updates an existing walletHolder.
     *
     * @param id the id of the walletHolder to save.
     * @param walletHolder the walletHolder to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated walletHolder,
     * or with status {@code 400 (Bad Request)} if the walletHolder is not valid,
     * or with status {@code 500 (Internal Server Error)} if the walletHolder couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<WalletHolder> updateWalletHolder(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody WalletHolder walletHolder
    ) throws URISyntaxException {
        LOG.debug("REST request to update WalletHolder : {}, {}", id, walletHolder);
        if (walletHolder.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, walletHolder.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!walletHolderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        walletHolder = walletHolderService.update(walletHolder);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, walletHolder.getId().toString()))
            .body(walletHolder);
    }

    /**
     * {@code PATCH  /wallet-holders/:id} : Partial updates given fields of an existing walletHolder, field will ignore if it is null
     *
     * @param id the id of the walletHolder to save.
     * @param walletHolder the walletHolder to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated walletHolder,
     * or with status {@code 400 (Bad Request)} if the walletHolder is not valid,
     * or with status {@code 404 (Not Found)} if the walletHolder is not found,
     * or with status {@code 500 (Internal Server Error)} if the walletHolder couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<WalletHolder> partialUpdateWalletHolder(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody WalletHolder walletHolder
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update WalletHolder partially : {}, {}", id, walletHolder);
        if (walletHolder.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, walletHolder.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!walletHolderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WalletHolder> result = walletHolderService.partialUpdate(walletHolder);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, walletHolder.getId().toString())
        );
    }

    /**
     * {@code GET  /wallet-holders} : get all the walletHolders.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of walletHolders in body.
     */
    @GetMapping("")
    public ResponseEntity<List<WalletHolder>> getAllWalletHolders(
        WalletHolderCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get WalletHolders by criteria: {}", criteria);

        Page<WalletHolder> page = walletHolderQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /wallet-holders/count} : count all the walletHolders.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countWalletHolders(WalletHolderCriteria criteria) {
        LOG.debug("REST request to count WalletHolders by criteria: {}", criteria);
        return ResponseEntity.ok().body(walletHolderQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /wallet-holders/:id} : get the "id" walletHolder.
     *
     * @param id the id of the walletHolder to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the walletHolder, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<WalletHolder> getWalletHolder(@PathVariable("id") Long id) {
        LOG.debug("REST request to get WalletHolder : {}", id);
        Optional<WalletHolder> walletHolder = walletHolderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(walletHolder);
    }

    /**
     * {@code DELETE  /wallet-holders/:id} : delete the "id" walletHolder.
     *
     * @param id the id of the walletHolder to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWalletHolder(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete WalletHolder : {}", id);
        walletHolderService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
