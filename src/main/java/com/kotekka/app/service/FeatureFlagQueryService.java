package com.kotekka.app.service;

import com.kotekka.app.domain.*; // for static metamodels
import com.kotekka.app.domain.FeatureFlag;
import com.kotekka.app.repository.FeatureFlagRepository;
import com.kotekka.app.service.criteria.FeatureFlagCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link FeatureFlag} entities in the database.
 * The main input is a {@link FeatureFlagCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link FeatureFlag} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FeatureFlagQueryService extends QueryService<FeatureFlag> {

    private static final Logger LOG = LoggerFactory.getLogger(FeatureFlagQueryService.class);

    private final FeatureFlagRepository featureFlagRepository;

    public FeatureFlagQueryService(FeatureFlagRepository featureFlagRepository) {
        this.featureFlagRepository = featureFlagRepository;
    }

    /**
     * Return a {@link Page} of {@link FeatureFlag} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FeatureFlag> findByCriteria(FeatureFlagCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FeatureFlag> specification = createSpecification(criteria);
        return featureFlagRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FeatureFlagCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<FeatureFlag> specification = createSpecification(criteria);
        return featureFlagRepository.count(specification);
    }

    /**
     * Function to convert {@link FeatureFlagCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FeatureFlag> createSpecification(FeatureFlagCriteria criteria) {
        Specification<FeatureFlag> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), FeatureFlag_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), FeatureFlag_.name));
            }
            if (criteria.getEnabled() != null) {
                specification = specification.and(buildSpecification(criteria.getEnabled(), FeatureFlag_.enabled));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), FeatureFlag_.description));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), FeatureFlag_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), FeatureFlag_.createdDate));
            }
            if (criteria.getUpdatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUpdatedBy(), FeatureFlag_.updatedBy));
            }
            if (criteria.getUpdatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedDate(), FeatureFlag_.updatedDate));
            }
        }
        return specification;
    }
}
