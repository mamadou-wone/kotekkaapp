package com.kotekka.app.service.impl;

import com.kotekka.app.domain.CacheInfo;
import com.kotekka.app.repository.CacheInfoRepository;
import com.kotekka.app.service.CacheInfoService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.kotekka.app.domain.CacheInfo}.
 */
@Service
@Transactional
public class CacheInfoServiceImpl implements CacheInfoService {

    private static final Logger LOG = LoggerFactory.getLogger(CacheInfoServiceImpl.class);

    private final CacheInfoRepository cacheInfoRepository;

    public CacheInfoServiceImpl(CacheInfoRepository cacheInfoRepository) {
        this.cacheInfoRepository = cacheInfoRepository;
    }

    @Override
    public CacheInfo save(CacheInfo cacheInfo) {
        LOG.debug("Request to save CacheInfo : {}", cacheInfo);
        return cacheInfoRepository.save(cacheInfo);
    }

    @Override
    public CacheInfo update(CacheInfo cacheInfo) {
        LOG.debug("Request to update CacheInfo : {}", cacheInfo);
        return cacheInfoRepository.save(cacheInfo);
    }

    @Override
    public Optional<CacheInfo> partialUpdate(CacheInfo cacheInfo) {
        LOG.debug("Request to partially update CacheInfo : {}", cacheInfo);

        return cacheInfoRepository
            .findById(cacheInfo.getId())
            .map(existingCacheInfo -> {
                if (cacheInfo.getWalletHolder() != null) {
                    existingCacheInfo.setWalletHolder(cacheInfo.getWalletHolder());
                }
                if (cacheInfo.getKey() != null) {
                    existingCacheInfo.setKey(cacheInfo.getKey());
                }
                if (cacheInfo.getValue() != null) {
                    existingCacheInfo.setValue(cacheInfo.getValue());
                }
                if (cacheInfo.getCreatedDate() != null) {
                    existingCacheInfo.setCreatedDate(cacheInfo.getCreatedDate());
                }

                return existingCacheInfo;
            })
            .map(cacheInfoRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CacheInfo> findOne(Long id) {
        LOG.debug("Request to get CacheInfo : {}", id);
        return cacheInfoRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete CacheInfo : {}", id);
        cacheInfoRepository.deleteById(id);
    }
}
