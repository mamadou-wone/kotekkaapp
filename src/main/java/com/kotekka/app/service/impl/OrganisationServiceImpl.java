package com.kotekka.app.service.impl;

import com.kotekka.app.domain.Organisation;
import com.kotekka.app.repository.OrganisationRepository;
import com.kotekka.app.service.OrganisationService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.kotekka.app.domain.Organisation}.
 */
@Service
@Transactional
public class OrganisationServiceImpl implements OrganisationService {

    private static final Logger LOG = LoggerFactory.getLogger(OrganisationServiceImpl.class);

    private final OrganisationRepository organisationRepository;

    public OrganisationServiceImpl(OrganisationRepository organisationRepository) {
        this.organisationRepository = organisationRepository;
    }

    @Override
    public Organisation save(Organisation organisation) {
        LOG.debug("Request to save Organisation : {}", organisation);
        return organisationRepository.save(organisation);
    }

    @Override
    public Organisation update(Organisation organisation) {
        LOG.debug("Request to update Organisation : {}", organisation);
        return organisationRepository.save(organisation);
    }

    @Override
    public Optional<Organisation> partialUpdate(Organisation organisation) {
        LOG.debug("Request to partially update Organisation : {}", organisation);

        return organisationRepository
            .findById(organisation.getId())
            .map(existingOrganisation -> {
                if (organisation.getName() != null) {
                    existingOrganisation.setName(organisation.getName());
                }
                if (organisation.getType() != null) {
                    existingOrganisation.setType(organisation.getType());
                }
                if (organisation.getParent() != null) {
                    existingOrganisation.setParent(organisation.getParent());
                }
                if (organisation.getLocation() != null) {
                    existingOrganisation.setLocation(organisation.getLocation());
                }
                if (organisation.getHeadcount() != null) {
                    existingOrganisation.setHeadcount(organisation.getHeadcount());
                }
                if (organisation.getCreatedBy() != null) {
                    existingOrganisation.setCreatedBy(organisation.getCreatedBy());
                }
                if (organisation.getCreatedDate() != null) {
                    existingOrganisation.setCreatedDate(organisation.getCreatedDate());
                }

                return existingOrganisation;
            })
            .map(organisationRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Organisation> findAll(Pageable pageable) {
        LOG.debug("Request to get all Organisations");
        return organisationRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Organisation> findOne(Long id) {
        LOG.debug("Request to get Organisation : {}", id);
        return organisationRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Organisation : {}", id);
        organisationRepository.deleteById(id);
    }
}
