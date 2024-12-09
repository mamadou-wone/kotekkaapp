package com.kotekka.app.service.impl;

import com.kotekka.app.domain.Cin;
import com.kotekka.app.repository.CinRepository;
import com.kotekka.app.service.CinService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.kotekka.app.domain.Cin}.
 */
@Service
@Transactional
public class CinServiceImpl implements CinService {

    private static final Logger LOG = LoggerFactory.getLogger(CinServiceImpl.class);

    private final CinRepository cinRepository;

    public CinServiceImpl(CinRepository cinRepository) {
        this.cinRepository = cinRepository;
    }

    @Override
    public Cin save(Cin cin) {
        LOG.debug("Request to save Cin : {}", cin);
        return cinRepository.save(cin);
    }

    @Override
    public Cin update(Cin cin) {
        LOG.debug("Request to update Cin : {}", cin);
        return cinRepository.save(cin);
    }

    @Override
    public Optional<Cin> partialUpdate(Cin cin) {
        LOG.debug("Request to partially update Cin : {}", cin);

        return cinRepository
            .findById(cin.getId())
            .map(existingCin -> {
                if (cin.getCinId() != null) {
                    existingCin.setCinId(cin.getCinId());
                }
                if (cin.getValidityDate() != null) {
                    existingCin.setValidityDate(cin.getValidityDate());
                }
                if (cin.getBirthDate() != null) {
                    existingCin.setBirthDate(cin.getBirthDate());
                }
                if (cin.getBirthPlace() != null) {
                    existingCin.setBirthPlace(cin.getBirthPlace());
                }
                if (cin.getFirstName() != null) {
                    existingCin.setFirstName(cin.getFirstName());
                }
                if (cin.getLastName() != null) {
                    existingCin.setLastName(cin.getLastName());
                }
                if (cin.getBirthCity() != null) {
                    existingCin.setBirthCity(cin.getBirthCity());
                }
                if (cin.getFatherName() != null) {
                    existingCin.setFatherName(cin.getFatherName());
                }
                if (cin.getNationality() != null) {
                    existingCin.setNationality(cin.getNationality());
                }
                if (cin.getNationalityCode() != null) {
                    existingCin.setNationalityCode(cin.getNationalityCode());
                }
                if (cin.getIssuingCountry() != null) {
                    existingCin.setIssuingCountry(cin.getIssuingCountry());
                }
                if (cin.getIssuingCountryCode() != null) {
                    existingCin.setIssuingCountryCode(cin.getIssuingCountryCode());
                }
                if (cin.getMotherName() != null) {
                    existingCin.setMotherName(cin.getMotherName());
                }
                if (cin.getCivilRegister() != null) {
                    existingCin.setCivilRegister(cin.getCivilRegister());
                }
                if (cin.getSex() != null) {
                    existingCin.setSex(cin.getSex());
                }
                if (cin.getAddress() != null) {
                    existingCin.setAddress(cin.getAddress());
                }
                if (cin.getBirthCityCode() != null) {
                    existingCin.setBirthCityCode(cin.getBirthCityCode());
                }
                if (cin.getWalletHolder() != null) {
                    existingCin.setWalletHolder(cin.getWalletHolder());
                }
                if (cin.getCreatedDate() != null) {
                    existingCin.setCreatedDate(cin.getCreatedDate());
                }

                return existingCin;
            })
            .map(cinRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Cin> findOne(Long id) {
        LOG.debug("Request to get Cin : {}", id);
        return cinRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Cin : {}", id);
        cinRepository.deleteById(id);
    }
}
