package com.kotekka.app.service.impl;

import com.kotekka.app.domain.Beneficiary;
import com.kotekka.app.repository.BeneficiaryRepository;
import com.kotekka.app.service.BeneficiaryService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.kotekka.app.domain.Beneficiary}.
 */
@Service
@Transactional
public class BeneficiaryServiceImpl implements BeneficiaryService {

    private static final Logger LOG = LoggerFactory.getLogger(BeneficiaryServiceImpl.class);

    private final BeneficiaryRepository beneficiaryRepository;

    public BeneficiaryServiceImpl(BeneficiaryRepository beneficiaryRepository) {
        this.beneficiaryRepository = beneficiaryRepository;
    }

    @Override
    public Beneficiary save(Beneficiary beneficiary) {
        LOG.debug("Request to save Beneficiary : {}", beneficiary);
        return beneficiaryRepository.save(beneficiary);
    }

    @Override
    public Beneficiary update(Beneficiary beneficiary) {
        LOG.debug("Request to update Beneficiary : {}", beneficiary);
        return beneficiaryRepository.save(beneficiary);
    }

    @Override
    public Optional<Beneficiary> partialUpdate(Beneficiary beneficiary) {
        LOG.debug("Request to partially update Beneficiary : {}", beneficiary);

        return beneficiaryRepository
            .findById(beneficiary.getId())
            .map(existingBeneficiary -> {
                if (beneficiary.getUuid() != null) {
                    existingBeneficiary.setUuid(beneficiary.getUuid());
                }
                if (beneficiary.getStatus() != null) {
                    existingBeneficiary.setStatus(beneficiary.getStatus());
                }
                if (beneficiary.getFirstName() != null) {
                    existingBeneficiary.setFirstName(beneficiary.getFirstName());
                }
                if (beneficiary.getLastName() != null) {
                    existingBeneficiary.setLastName(beneficiary.getLastName());
                }
                if (beneficiary.getCin() != null) {
                    existingBeneficiary.setCin(beneficiary.getCin());
                }
                if (beneficiary.getIban() != null) {
                    existingBeneficiary.setIban(beneficiary.getIban());
                }
                if (beneficiary.getPhoneNumber() != null) {
                    existingBeneficiary.setPhoneNumber(beneficiary.getPhoneNumber());
                }
                if (beneficiary.getEmail() != null) {
                    existingBeneficiary.setEmail(beneficiary.getEmail());
                }
                if (beneficiary.getWalletHolder() != null) {
                    existingBeneficiary.setWalletHolder(beneficiary.getWalletHolder());
                }
                if (beneficiary.getCreatedBy() != null) {
                    existingBeneficiary.setCreatedBy(beneficiary.getCreatedBy());
                }
                if (beneficiary.getCreatedDate() != null) {
                    existingBeneficiary.setCreatedDate(beneficiary.getCreatedDate());
                }
                if (beneficiary.getLastModifiedBy() != null) {
                    existingBeneficiary.setLastModifiedBy(beneficiary.getLastModifiedBy());
                }
                if (beneficiary.getLastModifiedDate() != null) {
                    existingBeneficiary.setLastModifiedDate(beneficiary.getLastModifiedDate());
                }

                return existingBeneficiary;
            })
            .map(beneficiaryRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Beneficiary> findAll(Pageable pageable) {
        LOG.debug("Request to get all Beneficiaries");
        return beneficiaryRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Beneficiary> findOne(Long id) {
        LOG.debug("Request to get Beneficiary : {}", id);
        return beneficiaryRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Beneficiary : {}", id);
        beneficiaryRepository.deleteById(id);
    }
}
