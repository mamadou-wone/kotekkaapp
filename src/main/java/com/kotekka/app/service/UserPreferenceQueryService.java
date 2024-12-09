package com.kotekka.app.service;

import com.kotekka.app.domain.*; // for static metamodels
import com.kotekka.app.domain.UserPreference;
import com.kotekka.app.repository.UserPreferenceRepository;
import com.kotekka.app.service.criteria.UserPreferenceCriteria;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link UserPreference} entities in the database.
 * The main input is a {@link UserPreferenceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link UserPreference} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UserPreferenceQueryService extends QueryService<UserPreference> {

    private static final Logger LOG = LoggerFactory.getLogger(UserPreferenceQueryService.class);

    private final UserPreferenceRepository userPreferenceRepository;

    public UserPreferenceQueryService(UserPreferenceRepository userPreferenceRepository) {
        this.userPreferenceRepository = userPreferenceRepository;
    }

    /**
     * Return a {@link Page} of {@link UserPreference} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<UserPreference> findByCriteria(UserPreferenceCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<UserPreference> specification = createSpecification(criteria);
        return userPreferenceRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UserPreferenceCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<UserPreference> specification = createSpecification(criteria);
        return userPreferenceRepository.count(specification);
    }

    /**
     * Function to convert {@link UserPreferenceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<UserPreference> createSpecification(UserPreferenceCriteria criteria) {
        Specification<UserPreference> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), UserPreference_.id));
            }
            if (criteria.getApp() != null) {
                specification = specification.and(buildSpecification(criteria.getApp(), UserPreference_.app));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), UserPreference_.name));
            }
            if (criteria.getSetting() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSetting(), UserPreference_.setting));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), UserPreference_.createdDate));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getUserId(), root -> root.join(UserPreference_.user, JoinType.LEFT).get(User_.id))
                );
            }
        }
        return specification;
    }
}
