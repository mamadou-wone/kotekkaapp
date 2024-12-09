package com.kotekka.app.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class FailedAttemptCriteriaTest {

    @Test
    void newFailedAttemptCriteriaHasAllFiltersNullTest() {
        var failedAttemptCriteria = new FailedAttemptCriteria();
        assertThat(failedAttemptCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void failedAttemptCriteriaFluentMethodsCreatesFiltersTest() {
        var failedAttemptCriteria = new FailedAttemptCriteria();

        setAllFilters(failedAttemptCriteria);

        assertThat(failedAttemptCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void failedAttemptCriteriaCopyCreatesNullFilterTest() {
        var failedAttemptCriteria = new FailedAttemptCriteria();
        var copy = failedAttemptCriteria.copy();

        assertThat(failedAttemptCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(failedAttemptCriteria)
        );
    }

    @Test
    void failedAttemptCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var failedAttemptCriteria = new FailedAttemptCriteria();
        setAllFilters(failedAttemptCriteria);

        var copy = failedAttemptCriteria.copy();

        assertThat(failedAttemptCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(failedAttemptCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var failedAttemptCriteria = new FailedAttemptCriteria();

        assertThat(failedAttemptCriteria).hasToString("FailedAttemptCriteria{}");
    }

    private static void setAllFilters(FailedAttemptCriteria failedAttemptCriteria) {
        failedAttemptCriteria.id();
        failedAttemptCriteria.login();
        failedAttemptCriteria.ipAddress();
        failedAttemptCriteria.isAfterLock();
        failedAttemptCriteria.app();
        failedAttemptCriteria.action();
        failedAttemptCriteria.device();
        failedAttemptCriteria.createdDate();
        failedAttemptCriteria.reason();
        failedAttemptCriteria.distinct();
    }

    private static Condition<FailedAttemptCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getLogin()) &&
                condition.apply(criteria.getIpAddress()) &&
                condition.apply(criteria.getIsAfterLock()) &&
                condition.apply(criteria.getApp()) &&
                condition.apply(criteria.getAction()) &&
                condition.apply(criteria.getDevice()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getReason()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<FailedAttemptCriteria> copyFiltersAre(
        FailedAttemptCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getLogin(), copy.getLogin()) &&
                condition.apply(criteria.getIpAddress(), copy.getIpAddress()) &&
                condition.apply(criteria.getIsAfterLock(), copy.getIsAfterLock()) &&
                condition.apply(criteria.getApp(), copy.getApp()) &&
                condition.apply(criteria.getAction(), copy.getAction()) &&
                condition.apply(criteria.getDevice(), copy.getDevice()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getReason(), copy.getReason()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
