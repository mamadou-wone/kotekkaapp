package com.kotekka.app.service.impl;

import com.kotekka.app.domain.WalletHolder;
import com.kotekka.app.repository.WalletHolderRepository;
import com.kotekka.app.service.WalletHolderService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.kotekka.app.domain.WalletHolder}.
 */
@Service
@Transactional
public class WalletHolderServiceImpl implements WalletHolderService {

    private static final Logger LOG = LoggerFactory.getLogger(WalletHolderServiceImpl.class);

    private final WalletHolderRepository walletHolderRepository;

    public WalletHolderServiceImpl(WalletHolderRepository walletHolderRepository) {
        this.walletHolderRepository = walletHolderRepository;
    }

    @Override
    public WalletHolder save(WalletHolder walletHolder) {
        LOG.debug("Request to save WalletHolder : {}", walletHolder);
        return walletHolderRepository.save(walletHolder);
    }

    @Override
    public WalletHolder update(WalletHolder walletHolder) {
        LOG.debug("Request to update WalletHolder : {}", walletHolder);
        return walletHolderRepository.save(walletHolder);
    }

    @Override
    public Optional<WalletHolder> partialUpdate(WalletHolder walletHolder) {
        LOG.debug("Request to partially update WalletHolder : {}", walletHolder);

        return walletHolderRepository
            .findById(walletHolder.getId())
            .map(existingWalletHolder -> {
                if (walletHolder.getUuid() != null) {
                    existingWalletHolder.setUuid(walletHolder.getUuid());
                }
                if (walletHolder.getType() != null) {
                    existingWalletHolder.setType(walletHolder.getType());
                }
                if (walletHolder.getStatus() != null) {
                    existingWalletHolder.setStatus(walletHolder.getStatus());
                }
                if (walletHolder.getPhoneNumber() != null) {
                    existingWalletHolder.setPhoneNumber(walletHolder.getPhoneNumber());
                }
                if (walletHolder.getNetwork() != null) {
                    existingWalletHolder.setNetwork(walletHolder.getNetwork());
                }
                if (walletHolder.getTag() != null) {
                    existingWalletHolder.setTag(walletHolder.getTag());
                }
                if (walletHolder.getFirstName() != null) {
                    existingWalletHolder.setFirstName(walletHolder.getFirstName());
                }
                if (walletHolder.getLastName() != null) {
                    existingWalletHolder.setLastName(walletHolder.getLastName());
                }
                if (walletHolder.getAddress() != null) {
                    existingWalletHolder.setAddress(walletHolder.getAddress());
                }
                if (walletHolder.getCity() != null) {
                    existingWalletHolder.setCity(walletHolder.getCity());
                }
                if (walletHolder.getCountry() != null) {
                    existingWalletHolder.setCountry(walletHolder.getCountry());
                }
                if (walletHolder.getPostalCode() != null) {
                    existingWalletHolder.setPostalCode(walletHolder.getPostalCode());
                }
                if (walletHolder.getOnboarding() != null) {
                    existingWalletHolder.setOnboarding(walletHolder.getOnboarding());
                }
                if (walletHolder.getExternalId() != null) {
                    existingWalletHolder.setExternalId(walletHolder.getExternalId());
                }
                if (walletHolder.getEmail() != null) {
                    existingWalletHolder.setEmail(walletHolder.getEmail());
                }
                if (walletHolder.getDateOfBirth() != null) {
                    existingWalletHolder.setDateOfBirth(walletHolder.getDateOfBirth());
                }
                if (walletHolder.getSex() != null) {
                    existingWalletHolder.setSex(walletHolder.getSex());
                }
                if (walletHolder.getCreatedBy() != null) {
                    existingWalletHolder.setCreatedBy(walletHolder.getCreatedBy());
                }
                if (walletHolder.getCreatedDate() != null) {
                    existingWalletHolder.setCreatedDate(walletHolder.getCreatedDate());
                }
                if (walletHolder.getLastModifiedBy() != null) {
                    existingWalletHolder.setLastModifiedBy(walletHolder.getLastModifiedBy());
                }
                if (walletHolder.getLastModifiedDate() != null) {
                    existingWalletHolder.setLastModifiedDate(walletHolder.getLastModifiedDate());
                }
                if (walletHolder.getLoginStatus() != null) {
                    existingWalletHolder.setLoginStatus(walletHolder.getLoginStatus());
                }

                return existingWalletHolder;
            })
            .map(walletHolderRepository::save);
    }

    public Page<WalletHolder> findAllWithEagerRelationships(Pageable pageable) {
        return walletHolderRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<WalletHolder> findOne(Long id) {
        LOG.debug("Request to get WalletHolder : {}", id);
        return walletHolderRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete WalletHolder : {}", id);
        walletHolderRepository.deleteById(id);
    }
}
