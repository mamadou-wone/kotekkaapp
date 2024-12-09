package com.kotekka.app.service;

import com.kotekka.app.domain.*; // for static metamodels
import com.kotekka.app.domain.CacheInfo;
import com.kotekka.app.repository.CacheInfoRepository;
import com.kotekka.app.service.criteria.CacheInfoCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link CacheInfo} entities in the database.
 * The main input is a {@link CacheInfoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CacheInfo} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CacheInfoQueryService extends QueryService<CacheInfo> {

    private static final Logger LOG = LoggerFactory.getLogger(CacheInfoQueryService.class);

    private final CacheInfoRepository cacheInfoRepository;

    public CacheInfoQueryService(CacheInfoRepository cacheInfoRepository) {
        this.cacheInfoRepository = cacheInfoRepository;
    }

    /**
     * Return a {@link Page} of {@link CacheInfo} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CacheInfo> findByCriteria(CacheInfoCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CacheInfo> specification = createSpecification(criteria);
        return cacheInfoRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CacheInfoCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<CacheInfo> specification = createSpecification(criteria);
        return cacheInfoRepository.count(specification);
    }

    /**
     * Function to convert {@link CacheInfoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CacheInfo> createSpecification(CacheInfoCriteria criteria) {
        Specification<CacheInfo> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CacheInfo_.id));
            }
            if (criteria.getWalletHolder() != null) {
                specification = specification.and(buildSpecification(criteria.getWalletHolder(), CacheInfo_.walletHolder));
            }
            if (criteria.getKey() != null) {
                specification = specification.and(buildStringSpecification(criteria.getKey(), CacheInfo_.key));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), CacheInfo_.createdDate));
            }
        }
        return specification;
    }
}
