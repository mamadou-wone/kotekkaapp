package com.kotekka.app.service;

import com.kotekka.app.domain.*; // for static metamodels
import com.kotekka.app.domain.ReferalInfo;
import com.kotekka.app.repository.ReferalInfoRepository;
import com.kotekka.app.service.criteria.ReferalInfoCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link ReferalInfo} entities in the database.
 * The main input is a {@link ReferalInfoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ReferalInfo} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ReferalInfoQueryService extends QueryService<ReferalInfo> {

    private static final Logger LOG = LoggerFactory.getLogger(ReferalInfoQueryService.class);

    private final ReferalInfoRepository referalInfoRepository;

    public ReferalInfoQueryService(ReferalInfoRepository referalInfoRepository) {
        this.referalInfoRepository = referalInfoRepository;
    }

    /**
     * Return a {@link Page} of {@link ReferalInfo} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ReferalInfo> findByCriteria(ReferalInfoCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ReferalInfo> specification = createSpecification(criteria);
        return referalInfoRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ReferalInfoCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ReferalInfo> specification = createSpecification(criteria);
        return referalInfoRepository.count(specification);
    }

    /**
     * Function to convert {@link ReferalInfoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ReferalInfo> createSpecification(ReferalInfoCriteria criteria) {
        Specification<ReferalInfo> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ReferalInfo_.id));
            }
            if (criteria.getUuid() != null) {
                specification = specification.and(buildSpecification(criteria.getUuid(), ReferalInfo_.uuid));
            }
            if (criteria.getReferalCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getReferalCode(), ReferalInfo_.referalCode));
            }
            if (criteria.getWalletHolder() != null) {
                specification = specification.and(buildSpecification(criteria.getWalletHolder(), ReferalInfo_.walletHolder));
            }
            if (criteria.getRefered() != null) {
                specification = specification.and(buildSpecification(criteria.getRefered(), ReferalInfo_.refered));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), ReferalInfo_.status));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), ReferalInfo_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), ReferalInfo_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), ReferalInfo_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), ReferalInfo_.lastModifiedDate));
            }
        }
        return specification;
    }
}
