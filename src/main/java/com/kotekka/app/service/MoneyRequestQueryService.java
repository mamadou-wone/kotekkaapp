package com.kotekka.app.service;

import com.kotekka.app.domain.*; // for static metamodels
import com.kotekka.app.domain.MoneyRequest;
import com.kotekka.app.repository.MoneyRequestRepository;
import com.kotekka.app.service.criteria.MoneyRequestCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link MoneyRequest} entities in the database.
 * The main input is a {@link MoneyRequestCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link MoneyRequest} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MoneyRequestQueryService extends QueryService<MoneyRequest> {

    private static final Logger LOG = LoggerFactory.getLogger(MoneyRequestQueryService.class);

    private final MoneyRequestRepository moneyRequestRepository;

    public MoneyRequestQueryService(MoneyRequestRepository moneyRequestRepository) {
        this.moneyRequestRepository = moneyRequestRepository;
    }

    /**
     * Return a {@link Page} of {@link MoneyRequest} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MoneyRequest> findByCriteria(MoneyRequestCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MoneyRequest> specification = createSpecification(criteria);
        return moneyRequestRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MoneyRequestCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<MoneyRequest> specification = createSpecification(criteria);
        return moneyRequestRepository.count(specification);
    }

    /**
     * Function to convert {@link MoneyRequestCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MoneyRequest> createSpecification(MoneyRequestCriteria criteria) {
        Specification<MoneyRequest> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), MoneyRequest_.id));
            }
            if (criteria.getUuid() != null) {
                specification = specification.and(buildSpecification(criteria.getUuid(), MoneyRequest_.uuid));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), MoneyRequest_.status));
            }
            if (criteria.getOtherHolder() != null) {
                specification = specification.and(buildSpecification(criteria.getOtherHolder(), MoneyRequest_.otherHolder));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), MoneyRequest_.amount));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), MoneyRequest_.description));
            }
            if (criteria.getCurrency() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCurrency(), MoneyRequest_.currency));
            }
            if (criteria.getWalletHolder() != null) {
                specification = specification.and(buildSpecification(criteria.getWalletHolder(), MoneyRequest_.walletHolder));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), MoneyRequest_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), MoneyRequest_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), MoneyRequest_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), MoneyRequest_.lastModifiedDate));
            }
        }
        return specification;
    }
}
