package com.kotekka.app.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class UserAffiliationCriteriaTest {

    @Test
    void newUserAffiliationCriteriaHasAllFiltersNullTest() {
        var userAffiliationCriteria = new UserAffiliationCriteria();
        assertThat(userAffiliationCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void userAffiliationCriteriaFluentMethodsCreatesFiltersTest() {
        var userAffiliationCriteria = new UserAffiliationCriteria();

        setAllFilters(userAffiliationCriteria);

        assertThat(userAffiliationCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void userAffiliationCriteriaCopyCreatesNullFilterTest() {
        var userAffiliationCriteria = new UserAffiliationCriteria();
        var copy = userAffiliationCriteria.copy();

        assertThat(userAffiliationCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(userAffiliationCriteria)
        );
    }

    @Test
    void userAffiliationCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var userAffiliationCriteria = new UserAffiliationCriteria();
        setAllFilters(userAffiliationCriteria);

        var copy = userAffiliationCriteria.copy();

        assertThat(userAffiliationCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(userAffiliationCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var userAffiliationCriteria = new UserAffiliationCriteria();

        assertThat(userAffiliationCriteria).hasToString("UserAffiliationCriteria{}");
    }

    private static void setAllFilters(UserAffiliationCriteria userAffiliationCriteria) {
        userAffiliationCriteria.id();
        userAffiliationCriteria.walletHolder();
        userAffiliationCriteria.affiliation();
        userAffiliationCriteria.createdBy();
        userAffiliationCriteria.createdDate();
        userAffiliationCriteria.distinct();
    }

    private static Condition<UserAffiliationCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getWalletHolder()) &&
                condition.apply(criteria.getAffiliation()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<UserAffiliationCriteria> copyFiltersAre(
        UserAffiliationCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getWalletHolder(), copy.getWalletHolder()) &&
                condition.apply(criteria.getAffiliation(), copy.getAffiliation()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
