package com.kotekka.app.service.impl;

import com.kotekka.app.domain.Order;
import com.kotekka.app.repository.OrderRepository;
import com.kotekka.app.service.OrderService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.kotekka.app.domain.Order}.
 */
@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private static final Logger LOG = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order save(Order order) {
        LOG.debug("Request to save Order : {}", order);
        return orderRepository.save(order);
    }

    @Override
    public Order update(Order order) {
        LOG.debug("Request to update Order : {}", order);
        return orderRepository.save(order);
    }

    @Override
    public Optional<Order> partialUpdate(Order order) {
        LOG.debug("Request to partially update Order : {}", order);

        return orderRepository
            .findById(order.getId())
            .map(existingOrder -> {
                if (order.getUuid() != null) {
                    existingOrder.setUuid(order.getUuid());
                }
                if (order.getWalletHolder() != null) {
                    existingOrder.setWalletHolder(order.getWalletHolder());
                }
                if (order.getStatus() != null) {
                    existingOrder.setStatus(order.getStatus());
                }
                if (order.getTotalPrice() != null) {
                    existingOrder.setTotalPrice(order.getTotalPrice());
                }
                if (order.getCurrency() != null) {
                    existingOrder.setCurrency(order.getCurrency());
                }
                if (order.getOrderDate() != null) {
                    existingOrder.setOrderDate(order.getOrderDate());
                }
                if (order.getPaymentMethod() != null) {
                    existingOrder.setPaymentMethod(order.getPaymentMethod());
                }
                if (order.getShippingAddress() != null) {
                    existingOrder.setShippingAddress(order.getShippingAddress());
                }
                if (order.getCreatedBy() != null) {
                    existingOrder.setCreatedBy(order.getCreatedBy());
                }
                if (order.getCreatedDate() != null) {
                    existingOrder.setCreatedDate(order.getCreatedDate());
                }
                if (order.getLastModifiedBy() != null) {
                    existingOrder.setLastModifiedBy(order.getLastModifiedBy());
                }
                if (order.getLastModifiedDate() != null) {
                    existingOrder.setLastModifiedDate(order.getLastModifiedDate());
                }

                return existingOrder;
            })
            .map(orderRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Order> findOne(Long id) {
        LOG.debug("Request to get Order : {}", id);
        return orderRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Order : {}", id);
        orderRepository.deleteById(id);
    }
}
