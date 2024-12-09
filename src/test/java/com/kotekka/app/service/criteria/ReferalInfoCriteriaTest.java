package com.kotekka.app.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ReferalInfoCriteriaTest {

    @Test
    void newReferalInfoCriteriaHasAllFiltersNullTest() {
        var referalInfoCriteria = new ReferalInfoCriteria();
        assertThat(referalInfoCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void referalInfoCriteriaFluentMethodsCreatesFiltersTest() {
        var referalInfoCriteria = new ReferalInfoCriteria();

        setAllFilters(referalInfoCriteria);

        assertThat(referalInfoCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void referalInfoCriteriaCopyCreatesNullFilterTest() {
        var referalInfoCriteria = new ReferalInfoCriteria();
        var copy = referalInfoCriteria.copy();

        assertThat(referalInfoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(referalInfoCriteria)
        );
    }

    @Test
    void referalInfoCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var referalInfoCriteria = new ReferalInfoCriteria();
        setAllFilters(referalInfoCriteria);

        var copy = referalInfoCriteria.copy();

        assertThat(referalInfoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(referalInfoCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var referalInfoCriteria = new ReferalInfoCriteria();

        assertThat(referalInfoCriteria).hasToString("ReferalInfoCriteria{}");
    }

    private static void setAllFilters(ReferalInfoCriteria referalInfoCriteria) {
        referalInfoCriteria.id();
        referalInfoCriteria.uuid();
        referalInfoCriteria.referalCode();
        referalInfoCriteria.walletHolder();
        referalInfoCriteria.refered();
        referalInfoCriteria.status();
        referalInfoCriteria.createdBy();
        referalInfoCriteria.createdDate();
        referalInfoCriteria.lastModifiedBy();
        referalInfoCriteria.lastModifiedDate();
        referalInfoCriteria.distinct();
    }

    private static Condition<ReferalInfoCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getUuid()) &&
                condition.apply(criteria.getReferalCode()) &&
                condition.apply(criteria.getWalletHolder()) &&
                condition.apply(criteria.getRefered()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ReferalInfoCriteria> copyFiltersAre(ReferalInfoCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getUuid(), copy.getUuid()) &&
                condition.apply(criteria.getReferalCode(), copy.getReferalCode()) &&
                condition.apply(criteria.getWalletHolder(), copy.getWalletHolder()) &&
                condition.apply(criteria.getRefered(), copy.getRefered()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy(), copy.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
