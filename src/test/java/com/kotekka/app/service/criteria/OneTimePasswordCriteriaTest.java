package com.kotekka.app.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class OneTimePasswordCriteriaTest {

    @Test
    void newOneTimePasswordCriteriaHasAllFiltersNullTest() {
        var oneTimePasswordCriteria = new OneTimePasswordCriteria();
        assertThat(oneTimePasswordCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void oneTimePasswordCriteriaFluentMethodsCreatesFiltersTest() {
        var oneTimePasswordCriteria = new OneTimePasswordCriteria();

        setAllFilters(oneTimePasswordCriteria);

        assertThat(oneTimePasswordCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void oneTimePasswordCriteriaCopyCreatesNullFilterTest() {
        var oneTimePasswordCriteria = new OneTimePasswordCriteria();
        var copy = oneTimePasswordCriteria.copy();

        assertThat(oneTimePasswordCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(oneTimePasswordCriteria)
        );
    }

    @Test
    void oneTimePasswordCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var oneTimePasswordCriteria = new OneTimePasswordCriteria();
        setAllFilters(oneTimePasswordCriteria);

        var copy = oneTimePasswordCriteria.copy();

        assertThat(oneTimePasswordCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(oneTimePasswordCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var oneTimePasswordCriteria = new OneTimePasswordCriteria();

        assertThat(oneTimePasswordCriteria).hasToString("OneTimePasswordCriteria{}");
    }

    private static void setAllFilters(OneTimePasswordCriteria oneTimePasswordCriteria) {
        oneTimePasswordCriteria.id();
        oneTimePasswordCriteria.uuid();
        oneTimePasswordCriteria.user();
        oneTimePasswordCriteria.code();
        oneTimePasswordCriteria.status();
        oneTimePasswordCriteria.expiry();
        oneTimePasswordCriteria.createdDate();
        oneTimePasswordCriteria.distinct();
    }

    private static Condition<OneTimePasswordCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getUuid()) &&
                condition.apply(criteria.getUser()) &&
                condition.apply(criteria.getCode()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getExpiry()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<OneTimePasswordCriteria> copyFiltersAre(
        OneTimePasswordCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getUuid(), copy.getUuid()) &&
                condition.apply(criteria.getUser(), copy.getUser()) &&
                condition.apply(criteria.getCode(), copy.getCode()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getExpiry(), copy.getExpiry()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
