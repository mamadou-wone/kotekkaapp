package com.kotekka.app.web.rest;

import com.kotekka.app.domain.UserAffiliation;
import com.kotekka.app.repository.UserAffiliationRepository;
import com.kotekka.app.service.UserAffiliationQueryService;
import com.kotekka.app.service.UserAffiliationService;
import com.kotekka.app.service.criteria.UserAffiliationCriteria;
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
 * REST controller for managing {@link com.kotekka.app.domain.UserAffiliation}.
 */
@RestController
@RequestMapping("/api/user-affiliations")
public class UserAffiliationResource {

    private static final Logger LOG = LoggerFactory.getLogger(UserAffiliationResource.class);

    private static final String ENTITY_NAME = "userAffiliation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserAffiliationService userAffiliationService;

    private final UserAffiliationRepository userAffiliationRepository;

    private final UserAffiliationQueryService userAffiliationQueryService;

    public UserAffiliationResource(
        UserAffiliationService userAffiliationService,
        UserAffiliationRepository userAffiliationRepository,
        UserAffiliationQueryService userAffiliationQueryService
    ) {
        this.userAffiliationService = userAffiliationService;
        this.userAffiliationRepository = userAffiliationRepository;
        this.userAffiliationQueryService = userAffiliationQueryService;
    }

    /**
     * {@code POST  /user-affiliations} : Create a new userAffiliation.
     *
     * @param userAffiliation the userAffiliation to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userAffiliation, or with status {@code 400 (Bad Request)} if the userAffiliation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<UserAffiliation> createUserAffiliation(@Valid @RequestBody UserAffiliation userAffiliation)
        throws URISyntaxException {
        LOG.debug("REST request to save UserAffiliation : {}", userAffiliation);
        if (userAffiliation.getId() != null) {
            throw new BadRequestAlertException("A new userAffiliation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        userAffiliation = userAffiliationService.save(userAffiliation);
        return ResponseEntity.created(new URI("/api/user-affiliations/" + userAffiliation.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, userAffiliation.getId().toString()))
            .body(userAffiliation);
    }

    /**
     * {@code PUT  /user-affiliations/:id} : Updates an existing userAffiliation.
     *
     * @param id the id of the userAffiliation to save.
     * @param userAffiliation the userAffiliation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userAffiliation,
     * or with status {@code 400 (Bad Request)} if the userAffiliation is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userAffiliation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserAffiliation> updateUserAffiliation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UserAffiliation userAffiliation
    ) throws URISyntaxException {
        LOG.debug("REST request to update UserAffiliation : {}, {}", id, userAffiliation);
        if (userAffiliation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userAffiliation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userAffiliationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        userAffiliation = userAffiliationService.update(userAffiliation);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userAffiliation.getId().toString()))
            .body(userAffiliation);
    }

    /**
     * {@code PATCH  /user-affiliations/:id} : Partial updates given fields of an existing userAffiliation, field will ignore if it is null
     *
     * @param id the id of the userAffiliation to save.
     * @param userAffiliation the userAffiliation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userAffiliation,
     * or with status {@code 400 (Bad Request)} if the userAffiliation is not valid,
     * or with status {@code 404 (Not Found)} if the userAffiliation is not found,
     * or with status {@code 500 (Internal Server Error)} if the userAffiliation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserAffiliation> partialUpdateUserAffiliation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UserAffiliation userAffiliation
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update UserAffiliation partially : {}, {}", id, userAffiliation);
        if (userAffiliation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userAffiliation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userAffiliationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserAffiliation> result = userAffiliationService.partialUpdate(userAffiliation);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userAffiliation.getId().toString())
        );
    }

    /**
     * {@code GET  /user-affiliations} : get all the userAffiliations.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userAffiliations in body.
     */
    @GetMapping("")
    public ResponseEntity<List<UserAffiliation>> getAllUserAffiliations(
        UserAffiliationCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get UserAffiliations by criteria: {}", criteria);

        Page<UserAffiliation> page = userAffiliationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /user-affiliations/count} : count all the userAffiliations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countUserAffiliations(UserAffiliationCriteria criteria) {
        LOG.debug("REST request to count UserAffiliations by criteria: {}", criteria);
        return ResponseEntity.ok().body(userAffiliationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /user-affiliations/:id} : get the "id" userAffiliation.
     *
     * @param id the id of the userAffiliation to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userAffiliation, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserAffiliation> getUserAffiliation(@PathVariable("id") Long id) {
        LOG.debug("REST request to get UserAffiliation : {}", id);
        Optional<UserAffiliation> userAffiliation = userAffiliationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userAffiliation);
    }

    /**
     * {@code DELETE  /user-affiliations/:id} : delete the "id" userAffiliation.
     *
     * @param id the id of the userAffiliation to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserAffiliation(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete UserAffiliation : {}", id);
        userAffiliationService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
