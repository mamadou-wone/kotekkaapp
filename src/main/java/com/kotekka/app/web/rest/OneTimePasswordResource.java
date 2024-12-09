package com.kotekka.app.web.rest;

import com.kotekka.app.domain.OneTimePassword;
import com.kotekka.app.repository.OneTimePasswordRepository;
import com.kotekka.app.service.OneTimePasswordQueryService;
import com.kotekka.app.service.OneTimePasswordService;
import com.kotekka.app.service.criteria.OneTimePasswordCriteria;
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
 * REST controller for managing {@link com.kotekka.app.domain.OneTimePassword}.
 */
@RestController
@RequestMapping("/api/one-time-passwords")
public class OneTimePasswordResource {

    private static final Logger LOG = LoggerFactory.getLogger(OneTimePasswordResource.class);

    private static final String ENTITY_NAME = "oneTimePassword";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OneTimePasswordService oneTimePasswordService;

    private final OneTimePasswordRepository oneTimePasswordRepository;

    private final OneTimePasswordQueryService oneTimePasswordQueryService;

    public OneTimePasswordResource(
        OneTimePasswordService oneTimePasswordService,
        OneTimePasswordRepository oneTimePasswordRepository,
        OneTimePasswordQueryService oneTimePasswordQueryService
    ) {
        this.oneTimePasswordService = oneTimePasswordService;
        this.oneTimePasswordRepository = oneTimePasswordRepository;
        this.oneTimePasswordQueryService = oneTimePasswordQueryService;
    }

    /**
     * {@code POST  /one-time-passwords} : Create a new oneTimePassword.
     *
     * @param oneTimePassword the oneTimePassword to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new oneTimePassword, or with status {@code 400 (Bad Request)} if the oneTimePassword has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<OneTimePassword> createOneTimePassword(@Valid @RequestBody OneTimePassword oneTimePassword)
        throws URISyntaxException {
        LOG.debug("REST request to save OneTimePassword : {}", oneTimePassword);
        if (oneTimePassword.getId() != null) {
            throw new BadRequestAlertException("A new oneTimePassword cannot already have an ID", ENTITY_NAME, "idexists");
        }
        oneTimePassword = oneTimePasswordService.save(oneTimePassword);
        return ResponseEntity.created(new URI("/api/one-time-passwords/" + oneTimePassword.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, oneTimePassword.getId().toString()))
            .body(oneTimePassword);
    }

    /**
     * {@code PUT  /one-time-passwords/:id} : Updates an existing oneTimePassword.
     *
     * @param id the id of the oneTimePassword to save.
     * @param oneTimePassword the oneTimePassword to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated oneTimePassword,
     * or with status {@code 400 (Bad Request)} if the oneTimePassword is not valid,
     * or with status {@code 500 (Internal Server Error)} if the oneTimePassword couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<OneTimePassword> updateOneTimePassword(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OneTimePassword oneTimePassword
    ) throws URISyntaxException {
        LOG.debug("REST request to update OneTimePassword : {}, {}", id, oneTimePassword);
        if (oneTimePassword.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, oneTimePassword.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!oneTimePasswordRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        oneTimePassword = oneTimePasswordService.update(oneTimePassword);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, oneTimePassword.getId().toString()))
            .body(oneTimePassword);
    }

    /**
     * {@code PATCH  /one-time-passwords/:id} : Partial updates given fields of an existing oneTimePassword, field will ignore if it is null
     *
     * @param id the id of the oneTimePassword to save.
     * @param oneTimePassword the oneTimePassword to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated oneTimePassword,
     * or with status {@code 400 (Bad Request)} if the oneTimePassword is not valid,
     * or with status {@code 404 (Not Found)} if the oneTimePassword is not found,
     * or with status {@code 500 (Internal Server Error)} if the oneTimePassword couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OneTimePassword> partialUpdateOneTimePassword(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OneTimePassword oneTimePassword
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update OneTimePassword partially : {}, {}", id, oneTimePassword);
        if (oneTimePassword.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, oneTimePassword.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!oneTimePasswordRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OneTimePassword> result = oneTimePasswordService.partialUpdate(oneTimePassword);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, oneTimePassword.getId().toString())
        );
    }

    /**
     * {@code GET  /one-time-passwords} : get all the oneTimePasswords.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of oneTimePasswords in body.
     */
    @GetMapping("")
    public ResponseEntity<List<OneTimePassword>> getAllOneTimePasswords(
        OneTimePasswordCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get OneTimePasswords by criteria: {}", criteria);

        Page<OneTimePassword> page = oneTimePasswordQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /one-time-passwords/count} : count all the oneTimePasswords.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countOneTimePasswords(OneTimePasswordCriteria criteria) {
        LOG.debug("REST request to count OneTimePasswords by criteria: {}", criteria);
        return ResponseEntity.ok().body(oneTimePasswordQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /one-time-passwords/:id} : get the "id" oneTimePassword.
     *
     * @param id the id of the oneTimePassword to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the oneTimePassword, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OneTimePassword> getOneTimePassword(@PathVariable("id") Long id) {
        LOG.debug("REST request to get OneTimePassword : {}", id);
        Optional<OneTimePassword> oneTimePassword = oneTimePasswordService.findOne(id);
        return ResponseUtil.wrapOrNotFound(oneTimePassword);
    }

    /**
     * {@code DELETE  /one-time-passwords/:id} : delete the "id" oneTimePassword.
     *
     * @param id the id of the oneTimePassword to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOneTimePassword(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete OneTimePassword : {}", id);
        oneTimePasswordService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
