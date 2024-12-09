package com.kotekka.app.service;

import com.kotekka.app.domain.*; // for static metamodels
import com.kotekka.app.domain.WalletHolder;
import com.kotekka.app.repository.WalletHolderRepository;
import com.kotekka.app.service.criteria.WalletHolderCriteria;
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
 * Service for executing complex queries for {@link WalletHolder} entities in the database.
 * The main input is a {@link WalletHolderCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link WalletHolder} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class WalletHolderQueryService extends QueryService<WalletHolder> {

    private static final Logger LOG = LoggerFactory.getLogger(WalletHolderQueryService.class);

    private final WalletHolderRepository walletHolderRepository;

    public WalletHolderQueryService(WalletHolderRepository walletHolderRepository) {
        this.walletHolderRepository = walletHolderRepository;
    }

    /**
     * Return a {@link Page} of {@link WalletHolder} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<WalletHolder> findByCriteria(WalletHolderCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<WalletHolder> specification = createSpecification(criteria);
        return walletHolderRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(WalletHolderCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<WalletHolder> specification = createSpecification(criteria);
        return walletHolderRepository.count(specification);
    }

    /**
     * Function to convert {@link WalletHolderCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<WalletHolder> createSpecification(WalletHolderCriteria criteria) {
        Specification<WalletHolder> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), WalletHolder_.id));
            }
            if (criteria.getUuid() != null) {
                specification = specification.and(buildSpecification(criteria.getUuid(), WalletHolder_.uuid));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildSpecification(criteria.getType(), WalletHolder_.type));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), WalletHolder_.status));
            }
            if (criteria.getPhoneNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneNumber(), WalletHolder_.phoneNumber));
            }
            if (criteria.getNetwork() != null) {
                specification = specification.and(buildSpecification(criteria.getNetwork(), WalletHolder_.network));
            }
            if (criteria.getTag() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTag(), WalletHolder_.tag));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), WalletHolder_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), WalletHolder_.lastName));
            }
            if (criteria.getAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddress(), WalletHolder_.address));
            }
            if (criteria.getCity() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCity(), WalletHolder_.city));
            }
            if (criteria.getCountry() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCountry(), WalletHolder_.country));
            }
            if (criteria.getPostalCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPostalCode(), WalletHolder_.postalCode));
            }
            if (criteria.getOnboarding() != null) {
                specification = specification.and(buildSpecification(criteria.getOnboarding(), WalletHolder_.onboarding));
            }
            if (criteria.getExternalId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExternalId(), WalletHolder_.externalId));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), WalletHolder_.email));
            }
            if (criteria.getDateOfBirth() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateOfBirth(), WalletHolder_.dateOfBirth));
            }
            if (criteria.getSex() != null) {
                specification = specification.and(buildSpecification(criteria.getSex(), WalletHolder_.sex));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), WalletHolder_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), WalletHolder_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), WalletHolder_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), WalletHolder_.lastModifiedDate));
            }
            if (criteria.getLoginStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getLoginStatus(), WalletHolder_.loginStatus));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getUserId(), root -> root.join(WalletHolder_.user, JoinType.LEFT).get(User_.id))
                );
            }
        }
        return specification;
    }
}
