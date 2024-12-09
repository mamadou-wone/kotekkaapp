package com.kotekka.app.service;

import com.kotekka.app.domain.*; // for static metamodels
import com.kotekka.app.domain.CartItem;
import com.kotekka.app.repository.CartItemRepository;
import com.kotekka.app.service.criteria.CartItemCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link CartItem} entities in the database.
 * The main input is a {@link CartItemCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CartItem} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CartItemQueryService extends QueryService<CartItem> {

    private static final Logger LOG = LoggerFactory.getLogger(CartItemQueryService.class);

    private final CartItemRepository cartItemRepository;

    public CartItemQueryService(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

    /**
     * Return a {@link Page} of {@link CartItem} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CartItem> findByCriteria(CartItemCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CartItem> specification = createSpecification(criteria);
        return cartItemRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CartItemCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<CartItem> specification = createSpecification(criteria);
        return cartItemRepository.count(specification);
    }

    /**
     * Function to convert {@link CartItemCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CartItem> createSpecification(CartItemCriteria criteria) {
        Specification<CartItem> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CartItem_.id));
            }
            if (criteria.getUuid() != null) {
                specification = specification.and(buildSpecification(criteria.getUuid(), CartItem_.uuid));
            }
            if (criteria.getCart() != null) {
                specification = specification.and(buildSpecification(criteria.getCart(), CartItem_.cart));
            }
            if (criteria.getProduct() != null) {
                specification = specification.and(buildSpecification(criteria.getProduct(), CartItem_.product));
            }
            if (criteria.getQuantity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQuantity(), CartItem_.quantity));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), CartItem_.price));
            }
            if (criteria.getTotalPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotalPrice(), CartItem_.totalPrice));
            }
        }
        return specification;
    }
}
