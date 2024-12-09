package com.kotekka.app.service.impl;

import com.kotekka.app.domain.Discount;
import com.kotekka.app.repository.DiscountRepository;
import com.kotekka.app.service.DiscountService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.kotekka.app.domain.Discount}.
 */
@Service
@Transactional
public class DiscountServiceImpl implements DiscountService {

    private static final Logger LOG = LoggerFactory.getLogger(DiscountServiceImpl.class);

    private final DiscountRepository discountRepository;

    public DiscountServiceImpl(DiscountRepository discountRepository) {
        this.discountRepository = discountRepository;
    }

    @Override
    public Discount save(Discount discount) {
        LOG.debug("Request to save Discount : {}", discount);
        return discountRepository.save(discount);
    }

    @Override
    public Discount update(Discount discount) {
        LOG.debug("Request to update Discount : {}", discount);
        return discountRepository.save(discount);
    }

    @Override
    public Optional<Discount> partialUpdate(Discount discount) {
        LOG.debug("Request to partially update Discount : {}", discount);

        return discountRepository
            .findById(discount.getId())
            .map(existingDiscount -> {
                if (discount.getUuid() != null) {
                    existingDiscount.setUuid(discount.getUuid());
                }
                if (discount.getName() != null) {
                    existingDiscount.setName(discount.getName());
                }
                if (discount.getType() != null) {
                    existingDiscount.setType(discount.getType());
                }
                if (discount.getValue() != null) {
                    existingDiscount.setValue(discount.getValue());
                }
                if (discount.getStartDate() != null) {
                    existingDiscount.setStartDate(discount.getStartDate());
                }
                if (discount.getEndDate() != null) {
                    existingDiscount.setEndDate(discount.getEndDate());
                }
                if (discount.getStatus() != null) {
                    existingDiscount.setStatus(discount.getStatus());
                }
                if (discount.getCreatedBy() != null) {
                    existingDiscount.setCreatedBy(discount.getCreatedBy());
                }
                if (discount.getCreatedDate() != null) {
                    existingDiscount.setCreatedDate(discount.getCreatedDate());
                }
                if (discount.getLastModifiedBy() != null) {
                    existingDiscount.setLastModifiedBy(discount.getLastModifiedBy());
                }
                if (discount.getLastModifiedDate() != null) {
                    existingDiscount.setLastModifiedDate(discount.getLastModifiedDate());
                }

                return existingDiscount;
            })
            .map(discountRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Discount> findOne(Long id) {
        LOG.debug("Request to get Discount : {}", id);
        return discountRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Discount : {}", id);
        discountRepository.deleteById(id);
    }
}
