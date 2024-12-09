package com.kotekka.app.service.impl;

import com.kotekka.app.domain.Wallet;
import com.kotekka.app.repository.WalletRepository;
import com.kotekka.app.service.WalletService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.kotekka.app.domain.Wallet}.
 */
@Service
@Transactional
public class WalletServiceImpl implements WalletService {

    private static final Logger LOG = LoggerFactory.getLogger(WalletServiceImpl.class);

    private final WalletRepository walletRepository;

    public WalletServiceImpl(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Override
    public Wallet save(Wallet wallet) {
        LOG.debug("Request to save Wallet : {}", wallet);
        return walletRepository.save(wallet);
    }

    @Override
    public Wallet update(Wallet wallet) {
        LOG.debug("Request to update Wallet : {}", wallet);
        return walletRepository.save(wallet);
    }

    @Override
    public Optional<Wallet> partialUpdate(Wallet wallet) {
        LOG.debug("Request to partially update Wallet : {}", wallet);

        return walletRepository
            .findById(wallet.getId())
            .map(existingWallet -> {
                if (wallet.getUuid() != null) {
                    existingWallet.setUuid(wallet.getUuid());
                }
                if (wallet.getType() != null) {
                    existingWallet.setType(wallet.getType());
                }
                if (wallet.getStatus() != null) {
                    existingWallet.setStatus(wallet.getStatus());
                }
                if (wallet.getLevel() != null) {
                    existingWallet.setLevel(wallet.getLevel());
                }
                if (wallet.getIban() != null) {
                    existingWallet.setIban(wallet.getIban());
                }
                if (wallet.getCurrency() != null) {
                    existingWallet.setCurrency(wallet.getCurrency());
                }
                if (wallet.getBalance() != null) {
                    existingWallet.setBalance(wallet.getBalance());
                }
                if (wallet.getBalancesAsOf() != null) {
                    existingWallet.setBalancesAsOf(wallet.getBalancesAsOf());
                }
                if (wallet.getExternalId() != null) {
                    existingWallet.setExternalId(wallet.getExternalId());
                }
                if (wallet.getWalletHolder() != null) {
                    existingWallet.setWalletHolder(wallet.getWalletHolder());
                }
                if (wallet.getCreatedBy() != null) {
                    existingWallet.setCreatedBy(wallet.getCreatedBy());
                }
                if (wallet.getCreatedDate() != null) {
                    existingWallet.setCreatedDate(wallet.getCreatedDate());
                }
                if (wallet.getLastModifiedBy() != null) {
                    existingWallet.setLastModifiedBy(wallet.getLastModifiedBy());
                }
                if (wallet.getLastModifiedDate() != null) {
                    existingWallet.setLastModifiedDate(wallet.getLastModifiedDate());
                }

                return existingWallet;
            })
            .map(walletRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Wallet> findOne(Long id) {
        LOG.debug("Request to get Wallet : {}", id);
        return walletRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Wallet : {}", id);
        walletRepository.deleteById(id);
    }
}
