package com.kotekka.app.service;

import com.kotekka.app.domain.*; // for static metamodels
import com.kotekka.app.domain.OneTimePassword;
import com.kotekka.app.repository.OneTimePasswordRepository;
import com.kotekka.app.service.criteria.OneTimePasswordCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link OneTimePassword} entities in the database.
 * The main input is a {@link OneTimePasswordCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link OneTimePassword} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OneTimePasswordQueryService extends QueryService<OneTimePassword> {

    private static final Logger LOG = LoggerFactory.getLogger(OneTimePasswordQueryService.class);

    private final OneTimePasswordRepository oneTimePasswordRepository;

    public OneTimePasswordQueryService(OneTimePasswordRepository oneTimePasswordRepository) {
        this.oneTimePasswordRepository = oneTimePasswordRepository;
    }

    /**
     * Return a {@link Page} of {@link OneTimePassword} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OneTimePassword> findByCriteria(OneTimePasswordCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<OneTimePassword> specification = createSpecification(criteria);
        return oneTimePasswordRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OneTimePasswordCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<OneTimePassword> specification = createSpecification(criteria);
        return oneTimePasswordRepository.count(specification);
    }

    /**
     * Function to convert {@link OneTimePasswordCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<OneTimePassword> createSpecification(OneTimePasswordCriteria criteria) {
        Specification<OneTimePassword> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), OneTimePassword_.id));
            }
            if (criteria.getUuid() != null) {
                specification = specification.and(buildSpecification(criteria.getUuid(), OneTimePassword_.uuid));
            }
            if (criteria.getUser() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUser(), OneTimePassword_.user));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), OneTimePassword_.code));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), OneTimePassword_.status));
            }
            if (criteria.getExpiry() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getExpiry(), OneTimePassword_.expiry));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), OneTimePassword_.createdDate));
            }
        }
        return specification;
    }
}
