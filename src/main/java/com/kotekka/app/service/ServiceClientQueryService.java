package com.kotekka.app.service;

import com.kotekka.app.domain.*; // for static metamodels
import com.kotekka.app.domain.ServiceClient;
import com.kotekka.app.repository.ServiceClientRepository;
import com.kotekka.app.service.criteria.ServiceClientCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link ServiceClient} entities in the database.
 * The main input is a {@link ServiceClientCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ServiceClient} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ServiceClientQueryService extends QueryService<ServiceClient> {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceClientQueryService.class);

    private final ServiceClientRepository serviceClientRepository;

    public ServiceClientQueryService(ServiceClientRepository serviceClientRepository) {
        this.serviceClientRepository = serviceClientRepository;
    }

    /**
     * Return a {@link Page} of {@link ServiceClient} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ServiceClient> findByCriteria(ServiceClientCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ServiceClient> specification = createSpecification(criteria);
        return serviceClientRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ServiceClientCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ServiceClient> specification = createSpecification(criteria);
        return serviceClientRepository.count(specification);
    }

    /**
     * Function to convert {@link ServiceClientCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ServiceClient> createSpecification(ServiceClientCriteria criteria) {
        Specification<ServiceClient> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ServiceClient_.id));
            }
            if (criteria.getClientId() != null) {
                specification = specification.and(buildSpecification(criteria.getClientId(), ServiceClient_.clientId));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildSpecification(criteria.getType(), ServiceClient_.type));
            }
            if (criteria.getApiKey() != null) {
                specification = specification.and(buildSpecification(criteria.getApiKey(), ServiceClient_.apiKey));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), ServiceClient_.status));
            }
            if (criteria.getNote() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNote(), ServiceClient_.note));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), ServiceClient_.createdDate));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), ServiceClient_.lastModifiedDate));
            }
        }
        return specification;
    }
}
