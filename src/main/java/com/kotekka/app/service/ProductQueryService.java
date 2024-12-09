package com.kotekka.app.service;

import com.kotekka.app.domain.*; // for static metamodels
import com.kotekka.app.domain.Product;
import com.kotekka.app.repository.ProductRepository;
import com.kotekka.app.service.criteria.ProductCriteria;
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
 * Service for executing complex queries for {@link Product} entities in the database.
 * The main input is a {@link ProductCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link Product} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductQueryService extends QueryService<Product> {

    private static final Logger LOG = LoggerFactory.getLogger(ProductQueryService.class);

    private final ProductRepository productRepository;

    public ProductQueryService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Return a {@link Page} of {@link Product} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Product> findByCriteria(ProductCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Product> specification = createSpecification(criteria);
        return productRepository.fetchBagRelationships(productRepository.findAll(specification, page));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Product> specification = createSpecification(criteria);
        return productRepository.count(specification);
    }

    /**
     * Function to convert {@link ProductCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Product> createSpecification(ProductCriteria criteria) {
        Specification<Product> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Product_.id));
            }
            if (criteria.getUuid() != null) {
                specification = specification.and(buildSpecification(criteria.getUuid(), Product_.uuid));
            }
            if (criteria.getWalletHolder() != null) {
                specification = specification.and(buildSpecification(criteria.getWalletHolder(), Product_.walletHolder));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Product_.title));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Product_.description));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), Product_.status));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), Product_.price));
            }
            if (criteria.getCompareAtPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCompareAtPrice(), Product_.compareAtPrice));
            }
            if (criteria.getCostPerItem() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCostPerItem(), Product_.costPerItem));
            }
            if (criteria.getProfit() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getProfit(), Product_.profit));
            }
            if (criteria.getMargin() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMargin(), Product_.margin));
            }
            if (criteria.getInventoryQuantity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getInventoryQuantity(), Product_.inventoryQuantity));
            }
            if (criteria.getInventoryLocation() != null) {
                specification = specification.and(buildStringSpecification(criteria.getInventoryLocation(), Product_.inventoryLocation));
            }
            if (criteria.getTrackQuantity() != null) {
                specification = specification.and(buildSpecification(criteria.getTrackQuantity(), Product_.trackQuantity));
            }
            if (criteria.getCategoryId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCategoryId(), root -> root.join(Product_.category, JoinType.LEFT).get(Category_.id))
                );
            }
            if (criteria.getCollectionsId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCollectionsId(), root ->
                        root.join(Product_.collections, JoinType.LEFT).get(Collection_.id)
                    )
                );
            }
        }
        return specification;
    }
}
