package com.kotekka.app.service.impl;

import com.kotekka.app.domain.ReferalInfo;
import com.kotekka.app.repository.ReferalInfoRepository;
import com.kotekka.app.service.ReferalInfoService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.kotekka.app.domain.ReferalInfo}.
 */
@Service
@Transactional
public class ReferalInfoServiceImpl implements ReferalInfoService {

    private static final Logger LOG = LoggerFactory.getLogger(ReferalInfoServiceImpl.class);

    private final ReferalInfoRepository referalInfoRepository;

    public ReferalInfoServiceImpl(ReferalInfoRepository referalInfoRepository) {
        this.referalInfoRepository = referalInfoRepository;
    }

    @Override
    public ReferalInfo save(ReferalInfo referalInfo) {
        LOG.debug("Request to save ReferalInfo : {}", referalInfo);
        return referalInfoRepository.save(referalInfo);
    }

    @Override
    public ReferalInfo update(ReferalInfo referalInfo) {
        LOG.debug("Request to update ReferalInfo : {}", referalInfo);
        return referalInfoRepository.save(referalInfo);
    }

    @Override
    public Optional<ReferalInfo> partialUpdate(ReferalInfo referalInfo) {
        LOG.debug("Request to partially update ReferalInfo : {}", referalInfo);

        return referalInfoRepository
            .findById(referalInfo.getId())
            .map(existingReferalInfo -> {
                if (referalInfo.getUuid() != null) {
                    existingReferalInfo.setUuid(referalInfo.getUuid());
                }
                if (referalInfo.getReferalCode() != null) {
                    existingReferalInfo.setReferalCode(referalInfo.getReferalCode());
                }
                if (referalInfo.getWalletHolder() != null) {
                    existingReferalInfo.setWalletHolder(referalInfo.getWalletHolder());
                }
                if (referalInfo.getRefered() != null) {
                    existingReferalInfo.setRefered(referalInfo.getRefered());
                }
                if (referalInfo.getStatus() != null) {
                    existingReferalInfo.setStatus(referalInfo.getStatus());
                }
                if (referalInfo.getCreatedBy() != null) {
                    existingReferalInfo.setCreatedBy(referalInfo.getCreatedBy());
                }
                if (referalInfo.getCreatedDate() != null) {
                    existingReferalInfo.setCreatedDate(referalInfo.getCreatedDate());
                }
                if (referalInfo.getLastModifiedBy() != null) {
                    existingReferalInfo.setLastModifiedBy(referalInfo.getLastModifiedBy());
                }
                if (referalInfo.getLastModifiedDate() != null) {
                    existingReferalInfo.setLastModifiedDate(referalInfo.getLastModifiedDate());
                }

                return existingReferalInfo;
            })
            .map(referalInfoRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ReferalInfo> findOne(Long id) {
        LOG.debug("Request to get ReferalInfo : {}", id);
        return referalInfoRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ReferalInfo : {}", id);
        referalInfoRepository.deleteById(id);
    }
}
