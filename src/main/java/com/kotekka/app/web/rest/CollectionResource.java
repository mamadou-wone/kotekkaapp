package com.kotekka.app.web.rest;

import com.kotekka.app.domain.Collection;
import com.kotekka.app.repository.CollectionRepository;
import com.kotekka.app.service.CollectionQueryService;
import com.kotekka.app.service.CollectionService;
import com.kotekka.app.service.criteria.CollectionCriteria;
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
 * REST controller for managing {@link com.kotekka.app.domain.Collection}.
 */
@RestController
@RequestMapping("/api/collections")
public class CollectionResource {

    private static final Logger LOG = LoggerFactory.getLogger(CollectionResource.class);

    private static final String ENTITY_NAME = "collection";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CollectionService collectionService;

    private final CollectionRepository collectionRepository;

    private final CollectionQueryService collectionQueryService;

    public CollectionResource(
        CollectionService collectionService,
        CollectionRepository collectionRepository,
        CollectionQueryService collectionQueryService
    ) {
        this.collectionService = collectionService;
        this.collectionRepository = collectionRepository;
        this.collectionQueryService = collectionQueryService;
    }

    /**
     * {@code POST  /collections} : Create a new collection.
     *
     * @param collection the collection to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new collection, or with status {@code 400 (Bad Request)} if the collection has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Collection> createCollection(@Valid @RequestBody Collection collection) throws URISyntaxException {
        LOG.debug("REST request to save Collection : {}", collection);
        if (collection.getId() != null) {
            throw new BadRequestAlertException("A new collection cannot already have an ID", ENTITY_NAME, "idexists");
        }
        collection = collectionService.save(collection);
        return ResponseEntity.created(new URI("/api/collections/" + collection.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, collection.getId().toString()))
            .body(collection);
    }

    /**
     * {@code PUT  /collections/:id} : Updates an existing collection.
     *
     * @param id the id of the collection to save.
     * @param collection the collection to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated collection,
     * or with status {@code 400 (Bad Request)} if the collection is not valid,
     * or with status {@code 500 (Internal Server Error)} if the collection couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Collection> updateCollection(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Collection collection
    ) throws URISyntaxException {
        LOG.debug("REST request to update Collection : {}, {}", id, collection);
        if (collection.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, collection.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!collectionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        collection = collectionService.update(collection);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, collection.getId().toString()))
            .body(collection);
    }

    /**
     * {@code PATCH  /collections/:id} : Partial updates given fields of an existing collection, field will ignore if it is null
     *
     * @param id the id of the collection to save.
     * @param collection the collection to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated collection,
     * or with status {@code 400 (Bad Request)} if the collection is not valid,
     * or with status {@code 404 (Not Found)} if the collection is not found,
     * or with status {@code 500 (Internal Server Error)} if the collection couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Collection> partialUpdateCollection(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Collection collection
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Collection partially : {}, {}", id, collection);
        if (collection.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, collection.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!collectionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Collection> result = collectionService.partialUpdate(collection);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, collection.getId().toString())
        );
    }

    /**
     * {@code GET  /collections} : get all the collections.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of collections in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Collection>> getAllCollections(
        CollectionCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Collections by criteria: {}", criteria);

        Page<Collection> page = collectionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /collections/count} : count all the collections.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCollections(CollectionCriteria criteria) {
        LOG.debug("REST request to count Collections by criteria: {}", criteria);
        return ResponseEntity.ok().body(collectionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /collections/:id} : get the "id" collection.
     *
     * @param id the id of the collection to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the collection, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Collection> getCollection(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Collection : {}", id);
        Optional<Collection> collection = collectionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(collection);
    }

    /**
     * {@code DELETE  /collections/:id} : delete the "id" collection.
     *
     * @param id the id of the collection to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCollection(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Collection : {}", id);
        collectionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
