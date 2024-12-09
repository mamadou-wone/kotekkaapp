package com.kotekka.app.service;

import com.kotekka.app.domain.*; // for static metamodels
import com.kotekka.app.domain.Cin;
import com.kotekka.app.repository.CinRepository;
import com.kotekka.app.service.criteria.CinCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Cin} entities in the database.
 * The main input is a {@link CinCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link Cin} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CinQueryService extends QueryService<Cin> {

    private static final Logger LOG = LoggerFactory.getLogger(CinQueryService.class);

    private final CinRepository cinRepository;

    public CinQueryService(CinRepository cinRepository) {
        this.cinRepository = cinRepository;
    }

    /**
     * Return a {@link Page} of {@link Cin} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Cin> findByCriteria(CinCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Cin> specification = createSpecification(criteria);
        return cinRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CinCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Cin> specification = createSpecification(criteria);
        return cinRepository.count(specification);
    }

    /**
     * Function to convert {@link CinCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Cin> createSpecification(CinCriteria criteria) {
        Specification<Cin> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Cin_.id));
            }
            if (criteria.getCinId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCinId(), Cin_.cinId));
            }
            if (criteria.getValidityDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getValidityDate(), Cin_.validityDate));
            }
            if (criteria.getBirthDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBirthDate(), Cin_.birthDate));
            }
            if (criteria.getBirthPlace() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBirthPlace(), Cin_.birthPlace));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), Cin_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), Cin_.lastName));
            }
            if (criteria.getBirthCity() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBirthCity(), Cin_.birthCity));
            }
            if (criteria.getFatherName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFatherName(), Cin_.fatherName));
            }
            if (criteria.getNationality() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNationality(), Cin_.nationality));
            }
            if (criteria.getNationalityCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNationalityCode(), Cin_.nationalityCode));
            }
            if (criteria.getIssuingCountry() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIssuingCountry(), Cin_.issuingCountry));
            }
            if (criteria.getIssuingCountryCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIssuingCountryCode(), Cin_.issuingCountryCode));
            }
            if (criteria.getMotherName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMotherName(), Cin_.motherName));
            }
            if (criteria.getCivilRegister() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCivilRegister(), Cin_.civilRegister));
            }
            if (criteria.getSex() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSex(), Cin_.sex));
            }
            if (criteria.getAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddress(), Cin_.address));
            }
            if (criteria.getBirthCityCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBirthCityCode(), Cin_.birthCityCode));
            }
            if (criteria.getWalletHolder() != null) {
                specification = specification.and(buildSpecification(criteria.getWalletHolder(), Cin_.walletHolder));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), Cin_.createdDate));
            }
        }
        return specification;
    }
}
