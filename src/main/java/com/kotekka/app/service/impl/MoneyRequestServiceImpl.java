package com.kotekka.app.service.impl;

import com.kotekka.app.domain.MoneyRequest;
import com.kotekka.app.repository.MoneyRequestRepository;
import com.kotekka.app.service.MoneyRequestService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.kotekka.app.domain.MoneyRequest}.
 */
@Service
@Transactional
public class MoneyRequestServiceImpl implements MoneyRequestService {

    private static final Logger LOG = LoggerFactory.getLogger(MoneyRequestServiceImpl.class);

    private final MoneyRequestRepository moneyRequestRepository;

    public MoneyRequestServiceImpl(MoneyRequestRepository moneyRequestRepository) {
        this.moneyRequestRepository = moneyRequestRepository;
    }

    @Override
    public MoneyRequest save(MoneyRequest moneyRequest) {
        LOG.debug("Request to save MoneyRequest : {}", moneyRequest);
        return moneyRequestRepository.save(moneyRequest);
    }

    @Override
    public MoneyRequest update(MoneyRequest moneyRequest) {
        LOG.debug("Request to update MoneyRequest : {}", moneyRequest);
        return moneyRequestRepository.save(moneyRequest);
    }

    @Override
    public Optional<MoneyRequest> partialUpdate(MoneyRequest moneyRequest) {
        LOG.debug("Request to partially update MoneyRequest : {}", moneyRequest);

        return moneyRequestRepository
            .findById(moneyRequest.getId())
            .map(existingMoneyRequest -> {
                if (moneyRequest.getUuid() != null) {
                    existingMoneyRequest.setUuid(moneyRequest.getUuid());
                }
                if (moneyRequest.getStatus() != null) {
                    existingMoneyRequest.setStatus(moneyRequest.getStatus());
                }
                if (moneyRequest.getOtherHolder() != null) {
                    existingMoneyRequest.setOtherHolder(moneyRequest.getOtherHolder());
                }
                if (moneyRequest.getAmount() != null) {
                    existingMoneyRequest.setAmount(moneyRequest.getAmount());
                }
                if (moneyRequest.getDescription() != null) {
                    existingMoneyRequest.setDescription(moneyRequest.getDescription());
                }
                if (moneyRequest.getCurrency() != null) {
                    existingMoneyRequest.setCurrency(moneyRequest.getCurrency());
                }
                if (moneyRequest.getWalletHolder() != null) {
                    existingMoneyRequest.setWalletHolder(moneyRequest.getWalletHolder());
                }
                if (moneyRequest.getCreatedBy() != null) {
                    existingMoneyRequest.setCreatedBy(moneyRequest.getCreatedBy());
                }
                if (moneyRequest.getCreatedDate() != null) {
                    existingMoneyRequest.setCreatedDate(moneyRequest.getCreatedDate());
                }
                if (moneyRequest.getLastModifiedBy() != null) {
                    existingMoneyRequest.setLastModifiedBy(moneyRequest.getLastModifiedBy());
                }
                if (moneyRequest.getLastModifiedDate() != null) {
                    existingMoneyRequest.setLastModifiedDate(moneyRequest.getLastModifiedDate());
                }

                return existingMoneyRequest;
            })
            .map(moneyRequestRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MoneyRequest> findOne(Long id) {
        LOG.debug("Request to get MoneyRequest : {}", id);
        return moneyRequestRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete MoneyRequest : {}", id);
        moneyRequestRepository.deleteById(id);
    }
}
