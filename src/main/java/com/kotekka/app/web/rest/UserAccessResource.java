package com.kotekka.app.web.rest;

import com.kotekka.app.domain.UserAccess;
import com.kotekka.app.repository.UserAccessRepository;
import com.kotekka.app.service.UserAccessQueryService;
import com.kotekka.app.service.UserAccessService;
import com.kotekka.app.service.criteria.UserAccessCriteria;
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
 * REST controller for managing {@link com.kotekka.app.domain.UserAccess}.
 */
@RestController
@RequestMapping("/api/user-accesses")
public class UserAccessResource {

    private static final Logger LOG = LoggerFactory.getLogger(UserAccessResource.class);

    private static final String ENTITY_NAME = "userAccess";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserAccessService userAccessService;

    private final UserAccessRepository userAccessRepository;

    private final UserAccessQueryService userAccessQueryService;

    public UserAccessResource(
        UserAccessService userAccessService,
        UserAccessRepository userAccessRepository,
        UserAccessQueryService userAccessQueryService
    ) {
        this.userAccessService = userAccessService;
        this.userAccessRepository = userAccessRepository;
        this.userAccessQueryService = userAccessQueryService;
    }

    /**
     * {@code POST  /user-accesses} : Create a new userAccess.
     *
     * @param userAccess the userAccess to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userAccess, or with status {@code 400 (Bad Request)} if the userAccess has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<UserAccess> createUserAccess(@Valid @RequestBody UserAccess userAccess) throws URISyntaxException {
        LOG.debug("REST request to save UserAccess : {}", userAccess);
        if (userAccess.getId() != null) {
            throw new BadRequestAlertException("A new userAccess cannot already have an ID", ENTITY_NAME, "idexists");
        }
        userAccess = userAccessService.save(userAccess);
        return ResponseEntity.created(new URI("/api/user-accesses/" + userAccess.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, userAccess.getId().toString()))
            .body(userAccess);
    }

    /**
     * {@code PUT  /user-accesses/:id} : Updates an existing userAccess.
     *
     * @param id the id of the userAccess to save.
     * @param userAccess the userAccess to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userAccess,
     * or with status {@code 400 (Bad Request)} if the userAccess is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userAccess couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserAccess> updateUserAccess(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UserAccess userAccess
    ) throws URISyntaxException {
        LOG.debug("REST request to update UserAccess : {}, {}", id, userAccess);
        if (userAccess.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userAccess.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userAccessRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        userAccess = userAccessService.update(userAccess);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userAccess.getId().toString()))
            .body(userAccess);
    }

    /**
     * {@code PATCH  /user-accesses/:id} : Partial updates given fields of an existing userAccess, field will ignore if it is null
     *
     * @param id the id of the userAccess to save.
     * @param userAccess the userAccess to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userAccess,
     * or with status {@code 400 (Bad Request)} if the userAccess is not valid,
     * or with status {@code 404 (Not Found)} if the userAccess is not found,
     * or with status {@code 500 (Internal Server Error)} if the userAccess couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserAccess> partialUpdateUserAccess(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UserAccess userAccess
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update UserAccess partially : {}, {}", id, userAccess);
        if (userAccess.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userAccess.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userAccessRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserAccess> result = userAccessService.partialUpdate(userAccess);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userAccess.getId().toString())
        );
    }

    /**
     * {@code GET  /user-accesses} : get all the userAccesses.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userAccesses in body.
     */
    @GetMapping("")
    public ResponseEntity<List<UserAccess>> getAllUserAccesses(
        UserAccessCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get UserAccesses by criteria: {}", criteria);

        Page<UserAccess> page = userAccessQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /user-accesses/count} : count all the userAccesses.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countUserAccesses(UserAccessCriteria criteria) {
        LOG.debug("REST request to count UserAccesses by criteria: {}", criteria);
        return ResponseEntity.ok().body(userAccessQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /user-accesses/:id} : get the "id" userAccess.
     *
     * @param id the id of the userAccess to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userAccess, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserAccess> getUserAccess(@PathVariable("id") Long id) {
        LOG.debug("REST request to get UserAccess : {}", id);
        Optional<UserAccess> userAccess = userAccessService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userAccess);
    }

    /**
     * {@code DELETE  /user-accesses/:id} : delete the "id" userAccess.
     *
     * @param id the id of the userAccess to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserAccess(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete UserAccess : {}", id);
        userAccessService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
