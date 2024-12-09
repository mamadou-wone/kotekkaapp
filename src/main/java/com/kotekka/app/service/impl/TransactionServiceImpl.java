package com.kotekka.app.service.impl;

import com.kotekka.app.domain.Transaction;
import com.kotekka.app.repository.TransactionRepository;
import com.kotekka.app.service.TransactionService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.kotekka.app.domain.Transaction}.
 */
@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionServiceImpl.class);

    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Transaction save(Transaction transaction) {
        LOG.debug("Request to save Transaction : {}", transaction);
        return transactionRepository.save(transaction);
    }

    @Override
    public Transaction update(Transaction transaction) {
        LOG.debug("Request to update Transaction : {}", transaction);
        return transactionRepository.save(transaction);
    }

    @Override
    public Optional<Transaction> partialUpdate(Transaction transaction) {
        LOG.debug("Request to partially update Transaction : {}", transaction);

        return transactionRepository
            .findById(transaction.getId())
            .map(existingTransaction -> {
                if (transaction.getUuid() != null) {
                    existingTransaction.setUuid(transaction.getUuid());
                }
                if (transaction.getType() != null) {
                    existingTransaction.setType(transaction.getType());
                }
                if (transaction.getStatus() != null) {
                    existingTransaction.setStatus(transaction.getStatus());
                }
                if (transaction.getDirection() != null) {
                    existingTransaction.setDirection(transaction.getDirection());
                }
                if (transaction.getAmount() != null) {
                    existingTransaction.setAmount(transaction.getAmount());
                }
                if (transaction.getDescription() != null) {
                    existingTransaction.setDescription(transaction.getDescription());
                }
                if (transaction.getFee() != null) {
                    existingTransaction.setFee(transaction.getFee());
                }
                if (transaction.getCommission() != null) {
                    existingTransaction.setCommission(transaction.getCommission());
                }
                if (transaction.getCurrency() != null) {
                    existingTransaction.setCurrency(transaction.getCurrency());
                }
                if (transaction.getCounterpartyType() != null) {
                    existingTransaction.setCounterpartyType(transaction.getCounterpartyType());
                }
                if (transaction.getCounterpartyId() != null) {
                    existingTransaction.setCounterpartyId(transaction.getCounterpartyId());
                }
                if (transaction.getEntryDate() != null) {
                    existingTransaction.setEntryDate(transaction.getEntryDate());
                }
                if (transaction.getEffectiveDate() != null) {
                    existingTransaction.setEffectiveDate(transaction.getEffectiveDate());
                }
                if (transaction.getStartTime() != null) {
                    existingTransaction.setStartTime(transaction.getStartTime());
                }
                if (transaction.getEndTime() != null) {
                    existingTransaction.setEndTime(transaction.getEndTime());
                }
                if (transaction.getParent() != null) {
                    existingTransaction.setParent(transaction.getParent());
                }
                if (transaction.getReference() != null) {
                    existingTransaction.setReference(transaction.getReference());
                }
                if (transaction.getExternalId() != null) {
                    existingTransaction.setExternalId(transaction.getExternalId());
                }
                if (transaction.getPartnerToken() != null) {
                    existingTransaction.setPartnerToken(transaction.getPartnerToken());
                }
                if (transaction.getWallet() != null) {
                    existingTransaction.setWallet(transaction.getWallet());
                }
                if (transaction.getCreatedBy() != null) {
                    existingTransaction.setCreatedBy(transaction.getCreatedBy());
                }
                if (transaction.getCreatedDate() != null) {
                    existingTransaction.setCreatedDate(transaction.getCreatedDate());
                }
                if (transaction.getLastModifiedBy() != null) {
                    existingTransaction.setLastModifiedBy(transaction.getLastModifiedBy());
                }
                if (transaction.getLastModifiedDate() != null) {
                    existingTransaction.setLastModifiedDate(transaction.getLastModifiedDate());
                }

                return existingTransaction;
            })
            .map(transactionRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Transaction> findOne(Long id) {
        LOG.debug("Request to get Transaction : {}", id);
        return transactionRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Transaction : {}", id);
        transactionRepository.deleteById(id);
    }
}
