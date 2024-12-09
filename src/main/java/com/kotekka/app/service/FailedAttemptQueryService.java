package com.kotekka.app.service;

import com.kotekka.app.domain.*; // for static metamodels
import com.kotekka.app.domain.FailedAttempt;
import com.kotekka.app.repository.FailedAttemptRepository;
import com.kotekka.app.service.criteria.FailedAttemptCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link FailedAttempt} entities in the database.
 * The main input is a {@link FailedAttemptCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link FailedAttempt} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FailedAttemptQueryService extends QueryService<FailedAttempt> {

    private static final Logger LOG = LoggerFactory.getLogger(FailedAttemptQueryService.class);

    private final FailedAttemptRepository failedAttemptRepository;

    public FailedAttemptQueryService(FailedAttemptRepository failedAttemptRepository) {
        this.failedAttemptRepository = failedAttemptRepository;
    }

    /**
     * Return a {@link Page} of {@link FailedAttempt} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FailedAttempt> findByCriteria(FailedAttemptCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FailedAttempt> specification = createSpecification(criteria);
        return failedAttemptRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FailedAttemptCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<FailedAttempt> specification = createSpecification(criteria);
        return failedAttemptRepository.count(specification);
    }

    /**
     * Function to convert {@link FailedAttemptCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FailedAttempt> createSpecification(FailedAttemptCriteria criteria) {
        Specification<FailedAttempt> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), FailedAttempt_.id));
            }
            if (criteria.getLogin() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLogin(), FailedAttempt_.login));
            }
            if (criteria.getIpAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIpAddress(), FailedAttempt_.ipAddress));
            }
            if (criteria.getIsAfterLock() != null) {
                specification = specification.and(buildSpecification(criteria.getIsAfterLock(), FailedAttempt_.isAfterLock));
            }
            if (criteria.getApp() != null) {
                specification = specification.and(buildSpecification(criteria.getApp(), FailedAttempt_.app));
            }
            if (criteria.getAction() != null) {
                specification = specification.and(buildSpecification(criteria.getAction(), FailedAttempt_.action));
            }
            if (criteria.getDevice() != null) {
                specification = specification.and(buildSpecification(criteria.getDevice(), FailedAttempt_.device));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), FailedAttempt_.createdDate));
            }
            if (criteria.getReason() != null) {
                specification = specification.and(buildStringSpecification(criteria.getReason(), FailedAttempt_.reason));
            }
        }
        return specification;
    }
}
