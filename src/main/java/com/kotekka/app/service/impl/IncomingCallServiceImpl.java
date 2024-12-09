package com.kotekka.app.service.impl;

import com.kotekka.app.domain.IncomingCall;
import com.kotekka.app.repository.IncomingCallRepository;
import com.kotekka.app.service.IncomingCallService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.kotekka.app.domain.IncomingCall}.
 */
@Service
@Transactional
public class IncomingCallServiceImpl implements IncomingCallService {

    private static final Logger LOG = LoggerFactory.getLogger(IncomingCallServiceImpl.class);

    private final IncomingCallRepository incomingCallRepository;

    public IncomingCallServiceImpl(IncomingCallRepository incomingCallRepository) {
        this.incomingCallRepository = incomingCallRepository;
    }

    @Override
    public IncomingCall save(IncomingCall incomingCall) {
        LOG.debug("Request to save IncomingCall : {}", incomingCall);
        return incomingCallRepository.save(incomingCall);
    }

    @Override
    public IncomingCall update(IncomingCall incomingCall) {
        LOG.debug("Request to update IncomingCall : {}", incomingCall);
        return incomingCallRepository.save(incomingCall);
    }

    @Override
    public Optional<IncomingCall> partialUpdate(IncomingCall incomingCall) {
        LOG.debug("Request to partially update IncomingCall : {}", incomingCall);

        return incomingCallRepository
            .findById(incomingCall.getId())
            .map(existingIncomingCall -> {
                if (incomingCall.getPartner() != null) {
                    existingIncomingCall.setPartner(incomingCall.getPartner());
                }
                if (incomingCall.getApi() != null) {
                    existingIncomingCall.setApi(incomingCall.getApi());
                }
                if (incomingCall.getMethod() != null) {
                    existingIncomingCall.setMethod(incomingCall.getMethod());
                }
                if (incomingCall.getRequestHeaders() != null) {
                    existingIncomingCall.setRequestHeaders(incomingCall.getRequestHeaders());
                }
                if (incomingCall.getRequestBody() != null) {
                    existingIncomingCall.setRequestBody(incomingCall.getRequestBody());
                }
                if (incomingCall.getCreatedDate() != null) {
                    existingIncomingCall.setCreatedDate(incomingCall.getCreatedDate());
                }
                if (incomingCall.getResponseStatusCode() != null) {
                    existingIncomingCall.setResponseStatusCode(incomingCall.getResponseStatusCode());
                }
                if (incomingCall.getResponseTime() != null) {
                    existingIncomingCall.setResponseTime(incomingCall.getResponseTime());
                }

                return existingIncomingCall;
            })
            .map(incomingCallRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<IncomingCall> findOne(Long id) {
        LOG.debug("Request to get IncomingCall : {}", id);
        return incomingCallRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete IncomingCall : {}", id);
        incomingCallRepository.deleteById(id);
    }
}
