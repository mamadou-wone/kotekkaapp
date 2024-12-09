package com.kotekka.app.web.rest;

import com.kotekka.app.domain.Beneficiary;
import com.kotekka.app.repository.BeneficiaryRepository;
import com.kotekka.app.service.BeneficiaryService;
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
 * REST controller for managing {@link com.kotekka.app.domain.Beneficiary}.
 */
@RestController
@RequestMapping("/api/beneficiaries")
public class BeneficiaryResource {

    private static final Logger LOG = LoggerFactory.getLogger(BeneficiaryResource.class);

    private static final String ENTITY_NAME = "beneficiary";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BeneficiaryService beneficiaryService;

    private final BeneficiaryRepository beneficiaryRepository;

    public BeneficiaryResource(BeneficiaryService beneficiaryService, BeneficiaryRepository beneficiaryRepository) {
        this.beneficiaryService = beneficiaryService;
        this.beneficiaryRepository = beneficiaryRepository;
    }

    /**
     * {@code POST  /beneficiaries} : Create a new beneficiary.
     *
     * @param beneficiary the beneficiary to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new beneficiary, or with status {@code 400 (Bad Request)} if the beneficiary has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Beneficiary> createBeneficiary(@Valid @RequestBody Beneficiary beneficiary) throws URISyntaxException {
        LOG.debug("REST request to save Beneficiary : {}", beneficiary);
        if (beneficiary.getId() != null) {
            throw new BadRequestAlertException("A new beneficiary cannot already have an ID", ENTITY_NAME, "idexists");
        }
        beneficiary = beneficiaryService.save(beneficiary);
        return ResponseEntity.created(new URI("/api/beneficiaries/" + beneficiary.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, beneficiary.getId().toString()))
            .body(beneficiary);
    }

    /**
     * {@code PUT  /beneficiaries/:id} : Updates an existing beneficiary.
     *
     * @param id the id of the beneficiary to save.
     * @param beneficiary the beneficiary to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated beneficiary,
     * or with status {@code 400 (Bad Request)} if the beneficiary is not valid,
     * or with status {@code 500 (Internal Server Error)} if the beneficiary couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Beneficiary> updateBeneficiary(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Beneficiary beneficiary
    ) throws URISyntaxException {
        LOG.debug("REST request to update Beneficiary : {}, {}", id, beneficiary);
        if (beneficiary.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, beneficiary.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!beneficiaryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        beneficiary = beneficiaryService.update(beneficiary);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, beneficiary.getId().toString()))
            .body(beneficiary);
    }

    /**
     * {@code PATCH  /beneficiaries/:id} : Partial updates given fields of an existing beneficiary, field will ignore if it is null
     *
     * @param id the id of the beneficiary to save.
     * @param beneficiary the beneficiary to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated beneficiary,
     * or with status {@code 400 (Bad Request)} if the beneficiary is not valid,
     * or with status {@code 404 (Not Found)} if the beneficiary is not found,
     * or with status {@code 500 (Internal Server Error)} if the beneficiary couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Beneficiary> partialUpdateBeneficiary(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Beneficiary beneficiary
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Beneficiary partially : {}, {}", id, beneficiary);
        if (beneficiary.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, beneficiary.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!beneficiaryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Beneficiary> result = beneficiaryService.partialUpdate(beneficiary);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, beneficiary.getId().toString())
        );
    }

    /**
     * {@code GET  /beneficiaries} : get all the beneficiaries.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of beneficiaries in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Beneficiary>> getAllBeneficiaries(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Beneficiaries");
        Page<Beneficiary> page = beneficiaryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /beneficiaries/:id} : get the "id" beneficiary.
     *
     * @param id the id of the beneficiary to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the beneficiary, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Beneficiary> getBeneficiary(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Beneficiary : {}", id);
        Optional<Beneficiary> beneficiary = beneficiaryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(beneficiary);
    }

    /**
     * {@code DELETE  /beneficiaries/:id} : delete the "id" beneficiary.
     *
     * @param id the id of the beneficiary to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBeneficiary(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Beneficiary : {}", id);
        beneficiaryService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
