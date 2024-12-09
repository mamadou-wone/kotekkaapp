package com.kotekka.app.service;

import com.kotekka.app.domain.*; // for static metamodels
import com.kotekka.app.domain.UserAccess;
import com.kotekka.app.repository.UserAccessRepository;
import com.kotekka.app.service.criteria.UserAccessCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link UserAccess} entities in the database.
 * The main input is a {@link UserAccessCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link UserAccess} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UserAccessQueryService extends QueryService<UserAccess> {

    private static final Logger LOG = LoggerFactory.getLogger(UserAccessQueryService.class);

    private final UserAccessRepository userAccessRepository;

    public UserAccessQueryService(UserAccessRepository userAccessRepository) {
        this.userAccessRepository = userAccessRepository;
    }

    /**
     * Return a {@link Page} of {@link UserAccess} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<UserAccess> findByCriteria(UserAccessCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<UserAccess> specification = createSpecification(criteria);
        return userAccessRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UserAccessCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<UserAccess> specification = createSpecification(criteria);
        return userAccessRepository.count(specification);
    }

    /**
     * Function to convert {@link UserAccessCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<UserAccess> createSpecification(UserAccessCriteria criteria) {
        Specification<UserAccess> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), UserAccess_.id));
            }
            if (criteria.getLogin() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLogin(), UserAccess_.login));
            }
            if (criteria.getIpAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIpAddress(), UserAccess_.ipAddress));
            }
            if (criteria.getDevice() != null) {
                specification = specification.and(buildSpecification(criteria.getDevice(), UserAccess_.device));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), UserAccess_.createdDate));
            }
        }
        return specification;
    }
}
