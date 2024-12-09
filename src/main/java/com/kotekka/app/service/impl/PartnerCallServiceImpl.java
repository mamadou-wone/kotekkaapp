package com.kotekka.app.service.impl;

import com.kotekka.app.domain.PartnerCall;
import com.kotekka.app.repository.PartnerCallRepository;
import com.kotekka.app.service.PartnerCallService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.kotekka.app.domain.PartnerCall}.
 */
@Service
@Transactional
public class PartnerCallServiceImpl implements PartnerCallService {

    private static final Logger LOG = LoggerFactory.getLogger(PartnerCallServiceImpl.class);

    private final PartnerCallRepository partnerCallRepository;

    public PartnerCallServiceImpl(PartnerCallRepository partnerCallRepository) {
        this.partnerCallRepository = partnerCallRepository;
    }

    @Override
    public PartnerCall save(PartnerCall partnerCall) {
        LOG.debug("Request to save PartnerCall : {}", partnerCall);
        return partnerCallRepository.save(partnerCall);
    }

    @Override
    public PartnerCall update(PartnerCall partnerCall) {
        LOG.debug("Request to update PartnerCall : {}", partnerCall);
        return partnerCallRepository.save(partnerCall);
    }

    @Override
    public Optional<PartnerCall> partialUpdate(PartnerCall partnerCall) {
        LOG.debug("Request to partially update PartnerCall : {}", partnerCall);

        return partnerCallRepository
            .findById(partnerCall.getId())
            .map(existingPartnerCall -> {
                if (partnerCall.getPartner() != null) {
                    existingPartnerCall.setPartner(partnerCall.getPartner());
                }
                if (partnerCall.getApi() != null) {
                    existingPartnerCall.setApi(partnerCall.getApi());
                }
                if (partnerCall.getMethod() != null) {
                    existingPartnerCall.setMethod(partnerCall.getMethod());
                }
                if (partnerCall.getRequestHeaders() != null) {
                    existingPartnerCall.setRequestHeaders(partnerCall.getRequestHeaders());
                }
                if (partnerCall.getRequestBody() != null) {
                    existingPartnerCall.setRequestBody(partnerCall.getRequestBody());
                }
                if (partnerCall.getRequestTime() != null) {
                    existingPartnerCall.setRequestTime(partnerCall.getRequestTime());
                }
                if (partnerCall.getResponseStatusCode() != null) {
                    existingPartnerCall.setResponseStatusCode(partnerCall.getResponseStatusCode());
                }
                if (partnerCall.getResponseHeaders() != null) {
                    existingPartnerCall.setResponseHeaders(partnerCall.getResponseHeaders());
                }
                if (partnerCall.getResponseBody() != null) {
                    existingPartnerCall.setResponseBody(partnerCall.getResponseBody());
                }
                if (partnerCall.getResponseTime() != null) {
                    existingPartnerCall.setResponseTime(partnerCall.getResponseTime());
                }
                if (partnerCall.getCorrelationId() != null) {
                    existingPartnerCall.setCorrelationId(partnerCall.getCorrelationId());
                }
                if (partnerCall.getQueryParam() != null) {
                    existingPartnerCall.setQueryParam(partnerCall.getQueryParam());
                }

                return existingPartnerCall;
            })
            .map(partnerCallRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PartnerCall> findOne(Long id) {
        LOG.debug("Request to get PartnerCall : {}", id);
        return partnerCallRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete PartnerCall : {}", id);
        partnerCallRepository.deleteById(id);
    }
}
