package com.kotekka.app.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class UserPreferenceCriteriaTest {

    @Test
    void newUserPreferenceCriteriaHasAllFiltersNullTest() {
        var userPreferenceCriteria = new UserPreferenceCriteria();
        assertThat(userPreferenceCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void userPreferenceCriteriaFluentMethodsCreatesFiltersTest() {
        var userPreferenceCriteria = new UserPreferenceCriteria();

        setAllFilters(userPreferenceCriteria);

        assertThat(userPreferenceCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void userPreferenceCriteriaCopyCreatesNullFilterTest() {
        var userPreferenceCriteria = new UserPreferenceCriteria();
        var copy = userPreferenceCriteria.copy();

        assertThat(userPreferenceCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(userPreferenceCriteria)
        );
    }

    @Test
    void userPreferenceCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var userPreferenceCriteria = new UserPreferenceCriteria();
        setAllFilters(userPreferenceCriteria);

        var copy = userPreferenceCriteria.copy();

        assertThat(userPreferenceCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(userPreferenceCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var userPreferenceCriteria = new UserPreferenceCriteria();

        assertThat(userPreferenceCriteria).hasToString("UserPreferenceCriteria{}");
    }

    private static void setAllFilters(UserPreferenceCriteria userPreferenceCriteria) {
        userPreferenceCriteria.id();
        userPreferenceCriteria.app();
        userPreferenceCriteria.name();
        userPreferenceCriteria.setting();
        userPreferenceCriteria.createdDate();
        userPreferenceCriteria.userId();
        userPreferenceCriteria.distinct();
    }

    private static Condition<UserPreferenceCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getApp()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getSetting()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getUserId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<UserPreferenceCriteria> copyFiltersAre(
        UserPreferenceCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getApp(), copy.getApp()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getSetting(), copy.getSetting()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getUserId(), copy.getUserId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
