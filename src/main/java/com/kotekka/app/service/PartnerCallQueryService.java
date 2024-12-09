package com.kotekka.app.service;

import com.kotekka.app.domain.*; // for static metamodels
import com.kotekka.app.domain.PartnerCall;
import com.kotekka.app.repository.PartnerCallRepository;
import com.kotekka.app.service.criteria.PartnerCallCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link PartnerCall} entities in the database.
 * The main input is a {@link PartnerCallCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link PartnerCall} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PartnerCallQueryService extends QueryService<PartnerCall> {

    private static final Logger LOG = LoggerFactory.getLogger(PartnerCallQueryService.class);

    private final PartnerCallRepository partnerCallRepository;

    public PartnerCallQueryService(PartnerCallRepository partnerCallRepository) {
        this.partnerCallRepository = partnerCallRepository;
    }

    /**
     * Return a {@link Page} of {@link PartnerCall} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PartnerCall> findByCriteria(PartnerCallCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PartnerCall> specification = createSpecification(criteria);
        return partnerCallRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PartnerCallCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<PartnerCall> specification = createSpecification(criteria);
        return partnerCallRepository.count(specification);
    }

    /**
     * Function to convert {@link PartnerCallCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PartnerCall> createSpecification(PartnerCallCriteria criteria) {
        Specification<PartnerCall> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PartnerCall_.id));
            }
            if (criteria.getPartner() != null) {
                specification = specification.and(buildSpecification(criteria.getPartner(), PartnerCall_.partner));
            }
            if (criteria.getApi() != null) {
                specification = specification.and(buildStringSpecification(criteria.getApi(), PartnerCall_.api));
            }
            if (criteria.getMethod() != null) {
                specification = specification.and(buildSpecification(criteria.getMethod(), PartnerCall_.method));
            }
            if (criteria.getRequestTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRequestTime(), PartnerCall_.requestTime));
            }
            if (criteria.getResponseStatusCode() != null) {
                specification = specification.and(
                    buildRangeSpecification(criteria.getResponseStatusCode(), PartnerCall_.responseStatusCode)
                );
            }
            if (criteria.getResponseTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getResponseTime(), PartnerCall_.responseTime));
            }
            if (criteria.getCorrelationId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCorrelationId(), PartnerCall_.correlationId));
            }
            if (criteria.getQueryParam() != null) {
                specification = specification.and(buildStringSpecification(criteria.getQueryParam(), PartnerCall_.queryParam));
            }
        }
        return specification;
    }
}
