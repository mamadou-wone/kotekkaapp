package com.kotekka.app.service.impl;

import com.kotekka.app.domain.Recipient;
import com.kotekka.app.repository.RecipientRepository;
import com.kotekka.app.service.RecipientService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.kotekka.app.domain.Recipient}.
 */
@Service
@Transactional
public class RecipientServiceImpl implements RecipientService {

    private static final Logger LOG = LoggerFactory.getLogger(RecipientServiceImpl.class);

    private final RecipientRepository recipientRepository;

    public RecipientServiceImpl(RecipientRepository recipientRepository) {
        this.recipientRepository = recipientRepository;
    }

    @Override
    public Recipient save(Recipient recipient) {
        LOG.debug("Request to save Recipient : {}", recipient);
        return recipientRepository.save(recipient);
    }

    @Override
    public Recipient update(Recipient recipient) {
        LOG.debug("Request to update Recipient : {}", recipient);
        return recipientRepository.save(recipient);
    }

    @Override
    public Optional<Recipient> partialUpdate(Recipient recipient) {
        LOG.debug("Request to partially update Recipient : {}", recipient);

        return recipientRepository
            .findById(recipient.getId())
            .map(existingRecipient -> {
                if (recipient.getUuid() != null) {
                    existingRecipient.setUuid(recipient.getUuid());
                }
                if (recipient.getStatus() != null) {
                    existingRecipient.setStatus(recipient.getStatus());
                }
                if (recipient.getFirstName() != null) {
                    existingRecipient.setFirstName(recipient.getFirstName());
                }
                if (recipient.getLastName() != null) {
                    existingRecipient.setLastName(recipient.getLastName());
                }
                if (recipient.getPhoneNumber() != null) {
                    existingRecipient.setPhoneNumber(recipient.getPhoneNumber());
                }
                if (recipient.getWalletHolder() != null) {
                    existingRecipient.setWalletHolder(recipient.getWalletHolder());
                }
                if (recipient.getCreatedBy() != null) {
                    existingRecipient.setCreatedBy(recipient.getCreatedBy());
                }
                if (recipient.getCreatedDate() != null) {
                    existingRecipient.setCreatedDate(recipient.getCreatedDate());
                }
                if (recipient.getLastModifiedBy() != null) {
                    existingRecipient.setLastModifiedBy(recipient.getLastModifiedBy());
                }
                if (recipient.getLastModifiedDate() != null) {
                    existingRecipient.setLastModifiedDate(recipient.getLastModifiedDate());
                }

                return existingRecipient;
            })
            .map(recipientRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Recipient> findAll(Pageable pageable) {
        LOG.debug("Request to get all Recipients");
        return recipientRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Recipient> findOne(Long id) {
        LOG.debug("Request to get Recipient : {}", id);
        return recipientRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Recipient : {}", id);
        recipientRepository.deleteById(id);
    }
}
