package com.kotekka.app.web.rest;

import com.kotekka.app.domain.ServiceClient;
import com.kotekka.app.repository.ServiceClientRepository;
import com.kotekka.app.service.ServiceClientQueryService;
import com.kotekka.app.service.ServiceClientService;
import com.kotekka.app.service.criteria.ServiceClientCriteria;
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
 * REST controller for managing {@link com.kotekka.app.domain.ServiceClient}.
 */
@RestController
@RequestMapping("/api/service-clients")
public class ServiceClientResource {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceClientResource.class);

    private static final String ENTITY_NAME = "serviceClient";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ServiceClientService serviceClientService;

    private final ServiceClientRepository serviceClientRepository;

    private final ServiceClientQueryService serviceClientQueryService;

    public ServiceClientResource(
        ServiceClientService serviceClientService,
        ServiceClientRepository serviceClientRepository,
        ServiceClientQueryService serviceClientQueryService
    ) {
        this.serviceClientService = serviceClientService;
        this.serviceClientRepository = serviceClientRepository;
        this.serviceClientQueryService = serviceClientQueryService;
    }

    /**
     * {@code POST  /service-clients} : Create a new serviceClient.
     *
     * @param serviceClient the serviceClient to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new serviceClient, or with status {@code 400 (Bad Request)} if the serviceClient has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ServiceClient> createServiceClient(@Valid @RequestBody ServiceClient serviceClient) throws URISyntaxException {
        LOG.debug("REST request to save ServiceClient : {}", serviceClient);
        if (serviceClient.getId() != null) {
            throw new BadRequestAlertException("A new serviceClient cannot already have an ID", ENTITY_NAME, "idexists");
        }
        serviceClient = serviceClientService.save(serviceClient);
        return ResponseEntity.created(new URI("/api/service-clients/" + serviceClient.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, serviceClient.getId().toString()))
            .body(serviceClient);
    }

    /**
     * {@code PUT  /service-clients/:id} : Updates an existing serviceClient.
     *
     * @param id the id of the serviceClient to save.
     * @param serviceClient the serviceClient to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated serviceClient,
     * or with status {@code 400 (Bad Request)} if the serviceClient is not valid,
     * or with status {@code 500 (Internal Server Error)} if the serviceClient couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ServiceClient> updateServiceClient(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ServiceClient serviceClient
    ) throws URISyntaxException {
        LOG.debug("REST request to update ServiceClient : {}, {}", id, serviceClient);
        if (serviceClient.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, serviceClient.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!serviceClientRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        serviceClient = serviceClientService.update(serviceClient);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, serviceClient.getId().toString()))
            .body(serviceClient);
    }

    /**
     * {@code PATCH  /service-clients/:id} : Partial updates given fields of an existing serviceClient, field will ignore if it is null
     *
     * @param id the id of the serviceClient to save.
     * @param serviceClient the serviceClient to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated serviceClient,
     * or with status {@code 400 (Bad Request)} if the serviceClient is not valid,
     * or with status {@code 404 (Not Found)} if the serviceClient is not found,
     * or with status {@code 500 (Internal Server Error)} if the serviceClient couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ServiceClient> partialUpdateServiceClient(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ServiceClient serviceClient
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ServiceClient partially : {}, {}", id, serviceClient);
        if (serviceClient.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, serviceClient.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!serviceClientRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ServiceClient> result = serviceClientService.partialUpdate(serviceClient);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, serviceClient.getId().toString())
        );
    }

    /**
     * {@code GET  /service-clients} : get all the serviceClients.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of serviceClients in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ServiceClient>> getAllServiceClients(
        ServiceClientCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ServiceClients by criteria: {}", criteria);

        Page<ServiceClient> page = serviceClientQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /service-clients/count} : count all the serviceClients.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countServiceClients(ServiceClientCriteria criteria) {
        LOG.debug("REST request to count ServiceClients by criteria: {}", criteria);
        return ResponseEntity.ok().body(serviceClientQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /service-clients/:id} : get the "id" serviceClient.
     *
     * @param id the id of the serviceClient to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the serviceClient, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ServiceClient> getServiceClient(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ServiceClient : {}", id);
        Optional<ServiceClient> serviceClient = serviceClientService.findOne(id);
        return ResponseUtil.wrapOrNotFound(serviceClient);
    }

    /**
     * {@code DELETE  /service-clients/:id} : delete the "id" serviceClient.
     *
     * @param id the id of the serviceClient to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteServiceClient(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ServiceClient : {}", id);
        serviceClientService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
