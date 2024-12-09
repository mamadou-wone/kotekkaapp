package com.kotekka.app.service;

import com.kotekka.app.domain.*; // for static metamodels
import com.kotekka.app.domain.UserAffiliation;
import com.kotekka.app.repository.UserAffiliationRepository;
import com.kotekka.app.service.criteria.UserAffiliationCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link UserAffiliation} entities in the database.
 * The main input is a {@link UserAffiliationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link UserAffiliation} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UserAffiliationQueryService extends QueryService<UserAffiliation> {

    private static final Logger LOG = LoggerFactory.getLogger(UserAffiliationQueryService.class);

    private final UserAffiliationRepository userAffiliationRepository;

    public UserAffiliationQueryService(UserAffiliationRepository userAffiliationRepository) {
        this.userAffiliationRepository = userAffiliationRepository;
    }

    /**
     * Return a {@link Page} of {@link UserAffiliation} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<UserAffiliation> findByCriteria(UserAffiliationCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<UserAffiliation> specification = createSpecification(criteria);
        return userAffiliationRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UserAffiliationCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<UserAffiliation> specification = createSpecification(criteria);
        return userAffiliationRepository.count(specification);
    }

    /**
     * Function to convert {@link UserAffiliationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<UserAffiliation> createSpecification(UserAffiliationCriteria criteria) {
        Specification<UserAffiliation> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), UserAffiliation_.id));
            }
            if (criteria.getWalletHolder() != null) {
                specification = specification.and(buildSpecification(criteria.getWalletHolder(), UserAffiliation_.walletHolder));
            }
            if (criteria.getAffiliation() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAffiliation(), UserAffiliation_.affiliation));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), UserAffiliation_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), UserAffiliation_.createdDate));
            }
        }
        return specification;
    }
}
