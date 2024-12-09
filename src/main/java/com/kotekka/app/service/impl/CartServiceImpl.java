package com.kotekka.app.service.impl;

import com.kotekka.app.domain.Cart;
import com.kotekka.app.repository.CartRepository;
import com.kotekka.app.service.CartService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.kotekka.app.domain.Cart}.
 */
@Service
@Transactional
public class CartServiceImpl implements CartService {

    private static final Logger LOG = LoggerFactory.getLogger(CartServiceImpl.class);

    private final CartRepository cartRepository;

    public CartServiceImpl(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Override
    public Cart save(Cart cart) {
        LOG.debug("Request to save Cart : {}", cart);
        return cartRepository.save(cart);
    }

    @Override
    public Cart update(Cart cart) {
        LOG.debug("Request to update Cart : {}", cart);
        return cartRepository.save(cart);
    }

    @Override
    public Optional<Cart> partialUpdate(Cart cart) {
        LOG.debug("Request to partially update Cart : {}", cart);

        return cartRepository
            .findById(cart.getId())
            .map(existingCart -> {
                if (cart.getUuid() != null) {
                    existingCart.setUuid(cart.getUuid());
                }
                if (cart.getWalletHolder() != null) {
                    existingCart.setWalletHolder(cart.getWalletHolder());
                }
                if (cart.getTotalQuantity() != null) {
                    existingCart.setTotalQuantity(cart.getTotalQuantity());
                }
                if (cart.getTotalPrice() != null) {
                    existingCart.setTotalPrice(cart.getTotalPrice());
                }
                if (cart.getCurrency() != null) {
                    existingCart.setCurrency(cart.getCurrency());
                }
                if (cart.getCreatedDate() != null) {
                    existingCart.setCreatedDate(cart.getCreatedDate());
                }
                if (cart.getLastModifiedDate() != null) {
                    existingCart.setLastModifiedDate(cart.getLastModifiedDate());
                }

                return existingCart;
            })
            .map(cartRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Cart> findOne(Long id) {
        LOG.debug("Request to get Cart : {}", id);
        return cartRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Cart : {}", id);
        cartRepository.deleteById(id);
    }
}
