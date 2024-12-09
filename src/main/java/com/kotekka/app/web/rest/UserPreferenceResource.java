package com.kotekka.app.web.rest;

import com.kotekka.app.domain.UserPreference;
import com.kotekka.app.repository.UserPreferenceRepository;
import com.kotekka.app.service.UserPreferenceQueryService;
import com.kotekka.app.service.UserPreferenceService;
import com.kotekka.app.service.criteria.UserPreferenceCriteria;
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
 * REST controller for managing {@link com.kotekka.app.domain.UserPreference}.
 */
@RestController
@RequestMapping("/api/user-preferences")
public class UserPreferenceResource {

    private static final Logger LOG = LoggerFactory.getLogger(UserPreferenceResource.class);

    private static final String ENTITY_NAME = "userPreference";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserPreferenceService userPreferenceService;

    private final UserPreferenceRepository userPreferenceRepository;

    private final UserPreferenceQueryService userPreferenceQueryService;

    public UserPreferenceResource(
        UserPreferenceService userPreferenceService,
        UserPreferenceRepository userPreferenceRepository,
        UserPreferenceQueryService userPreferenceQueryService
    ) {
        this.userPreferenceService = userPreferenceService;
        this.userPreferenceRepository = userPreferenceRepository;
        this.userPreferenceQueryService = userPreferenceQueryService;
    }

    /**
     * {@code POST  /user-preferences} : Create a new userPreference.
     *
     * @param userPreference the userPreference to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userPreference, or with status {@code 400 (Bad Request)} if the userPreference has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<UserPreference> createUserPreference(@Valid @RequestBody UserPreference userPreference)
        throws URISyntaxException {
        LOG.debug("REST request to save UserPreference : {}", userPreference);
        if (userPreference.getId() != null) {
            throw new BadRequestAlertException("A new userPreference cannot already have an ID", ENTITY_NAME, "idexists");
        }
        userPreference = userPreferenceService.save(userPreference);
        return ResponseEntity.created(new URI("/api/user-preferences/" + userPreference.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, userPreference.getId().toString()))
            .body(userPreference);
    }

    /**
     * {@code PUT  /user-preferences/:id} : Updates an existing userPreference.
     *
     * @param id the id of the userPreference to save.
     * @param userPreference the userPreference to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userPreference,
     * or with status {@code 400 (Bad Request)} if the userPreference is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userPreference couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserPreference> updateUserPreference(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UserPreference userPreference
    ) throws URISyntaxException {
        LOG.debug("REST request to update UserPreference : {}, {}", id, userPreference);
        if (userPreference.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userPreference.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userPreferenceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        userPreference = userPreferenceService.update(userPreference);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userPreference.getId().toString()))
            .body(userPreference);
    }

    /**
     * {@code PATCH  /user-preferences/:id} : Partial updates given fields of an existing userPreference, field will ignore if it is null
     *
     * @param id the id of the userPreference to save.
     * @param userPreference the userPreference to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userPreference,
     * or with status {@code 400 (Bad Request)} if the userPreference is not valid,
     * or with status {@code 404 (Not Found)} if the userPreference is not found,
     * or with status {@code 500 (Internal Server Error)} if the userPreference couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserPreference> partialUpdateUserPreference(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UserPreference userPreference
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update UserPreference partially : {}, {}", id, userPreference);
        if (userPreference.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userPreference.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userPreferenceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserPreference> result = userPreferenceService.partialUpdate(userPreference);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userPreference.getId().toString())
        );
    }

    /**
     * {@code GET  /user-preferences} : get all the userPreferences.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userPreferences in body.
     */
    @GetMapping("")
    public ResponseEntity<List<UserPreference>> getAllUserPreferences(
        UserPreferenceCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get UserPreferences by criteria: {}", criteria);

        Page<UserPreference> page = userPreferenceQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /user-preferences/count} : count all the userPreferences.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countUserPreferences(UserPreferenceCriteria criteria) {
        LOG.debug("REST request to count UserPreferences by criteria: {}", criteria);
        return ResponseEntity.ok().body(userPreferenceQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /user-preferences/:id} : get the "id" userPreference.
     *
     * @param id the id of the userPreference to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userPreference, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserPreference> getUserPreference(@PathVariable("id") Long id) {
        LOG.debug("REST request to get UserPreference : {}", id);
        Optional<UserPreference> userPreference = userPreferenceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userPreference);
    }

    /**
     * {@code DELETE  /user-preferences/:id} : delete the "id" userPreference.
     *
     * @param id the id of the userPreference to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserPreference(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete UserPreference : {}", id);
        userPreferenceService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
