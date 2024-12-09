package com.kotekka.app.service.impl;

import com.kotekka.app.domain.CartItem;
import com.kotekka.app.repository.CartItemRepository;
import com.kotekka.app.service.CartItemService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.kotekka.app.domain.CartItem}.
 */
@Service
@Transactional
public class CartItemServiceImpl implements CartItemService {

    private static final Logger LOG = LoggerFactory.getLogger(CartItemServiceImpl.class);

    private final CartItemRepository cartItemRepository;

    public CartItemServiceImpl(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    public CartItem save(CartItem cartItem) {
        LOG.debug("Request to save CartItem : {}", cartItem);
        return cartItemRepository.save(cartItem);
    }

    @Override
    public CartItem update(CartItem cartItem) {
        LOG.debug("Request to update CartItem : {}", cartItem);
        return cartItemRepository.save(cartItem);
    }

    @Override
    public Optional<CartItem> partialUpdate(CartItem cartItem) {
        LOG.debug("Request to partially update CartItem : {}", cartItem);

        return cartItemRepository
            .findById(cartItem.getId())
            .map(existingCartItem -> {
                if (cartItem.getUuid() != null) {
                    existingCartItem.setUuid(cartItem.getUuid());
                }
                if (cartItem.getCart() != null) {
                    existingCartItem.setCart(cartItem.getCart());
                }
                if (cartItem.getProduct() != null) {
                    existingCartItem.setProduct(cartItem.getProduct());
                }
                if (cartItem.getQuantity() != null) {
                    existingCartItem.setQuantity(cartItem.getQuantity());
                }
                if (cartItem.getPrice() != null) {
                    existingCartItem.setPrice(cartItem.getPrice());
                }
                if (cartItem.getTotalPrice() != null) {
                    existingCartItem.setTotalPrice(cartItem.getTotalPrice());
                }

                return existingCartItem;
            })
            .map(cartItemRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CartItem> findOne(Long id) {
        LOG.debug("Request to get CartItem : {}", id);
        return cartItemRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete CartItem : {}", id);
        cartItemRepository.deleteById(id);
    }
}
