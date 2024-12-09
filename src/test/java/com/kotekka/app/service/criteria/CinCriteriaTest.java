package com.kotekka.app.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CinCriteriaTest {

    @Test
    void newCinCriteriaHasAllFiltersNullTest() {
        var cinCriteria = new CinCriteria();
        assertThat(cinCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void cinCriteriaFluentMethodsCreatesFiltersTest() {
        var cinCriteria = new CinCriteria();

        setAllFilters(cinCriteria);

        assertThat(cinCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void cinCriteriaCopyCreatesNullFilterTest() {
        var cinCriteria = new CinCriteria();
        var copy = cinCriteria.copy();

        assertThat(cinCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(cinCriteria)
        );
    }

    @Test
    void cinCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var cinCriteria = new CinCriteria();
        setAllFilters(cinCriteria);

        var copy = cinCriteria.copy();

        assertThat(cinCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(cinCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var cinCriteria = new CinCriteria();

        assertThat(cinCriteria).hasToString("CinCriteria{}");
    }

    private static void setAllFilters(CinCriteria cinCriteria) {
        cinCriteria.id();
        cinCriteria.cinId();
        cinCriteria.validityDate();
        cinCriteria.birthDate();
        cinCriteria.birthPlace();
        cinCriteria.firstName();
        cinCriteria.lastName();
        cinCriteria.birthCity();
        cinCriteria.fatherName();
        cinCriteria.nationality();
        cinCriteria.nationalityCode();
        cinCriteria.issuingCountry();
        cinCriteria.issuingCountryCode();
        cinCriteria.motherName();
        cinCriteria.civilRegister();
        cinCriteria.sex();
        cinCriteria.address();
        cinCriteria.birthCityCode();
        cinCriteria.walletHolder();
        cinCriteria.createdDate();
        cinCriteria.distinct();
    }

    private static Condition<CinCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCinId()) &&
                condition.apply(criteria.getValidityDate()) &&
                condition.apply(criteria.getBirthDate()) &&
                condition.apply(criteria.getBirthPlace()) &&
                condition.apply(criteria.getFirstName()) &&
                condition.apply(criteria.getLastName()) &&
                condition.apply(criteria.getBirthCity()) &&
                condition.apply(criteria.getFatherName()) &&
                condition.apply(criteria.getNationality()) &&
                condition.apply(criteria.getNationalityCode()) &&
                condition.apply(criteria.getIssuingCountry()) &&
                condition.apply(criteria.getIssuingCountryCode()) &&
                condition.apply(criteria.getMotherName()) &&
                condition.apply(criteria.getCivilRegister()) &&
                condition.apply(criteria.getSex()) &&
                condition.apply(criteria.getAddress()) &&
                condition.apply(criteria.getBirthCityCode()) &&
                condition.apply(criteria.getWalletHolder()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CinCriteria> copyFiltersAre(CinCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCinId(), copy.getCinId()) &&
                condition.apply(criteria.getValidityDate(), copy.getValidityDate()) &&
                condition.apply(criteria.getBirthDate(), copy.getBirthDate()) &&
                condition.apply(criteria.getBirthPlace(), copy.getBirthPlace()) &&
                condition.apply(criteria.getFirstName(), copy.getFirstName()) &&
                condition.apply(criteria.getLastName(), copy.getLastName()) &&
                condition.apply(criteria.getBirthCity(), copy.getBirthCity()) &&
                condition.apply(criteria.getFatherName(), copy.getFatherName()) &&
                condition.apply(criteria.getNationality(), copy.getNationality()) &&
                condition.apply(criteria.getNationalityCode(), copy.getNationalityCode()) &&
                condition.apply(criteria.getIssuingCountry(), copy.getIssuingCountry()) &&
                condition.apply(criteria.getIssuingCountryCode(), copy.getIssuingCountryCode()) &&
                condition.apply(criteria.getMotherName(), copy.getMotherName()) &&
                condition.apply(criteria.getCivilRegister(), copy.getCivilRegister()) &&
                condition.apply(criteria.getSex(), copy.getSex()) &&
                condition.apply(criteria.getAddress(), copy.getAddress()) &&
                condition.apply(criteria.getBirthCityCode(), copy.getBirthCityCode()) &&
                condition.apply(criteria.getWalletHolder(), copy.getWalletHolder()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
