package com.kotekka.app.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class UserAccessCriteriaTest {

    @Test
    void newUserAccessCriteriaHasAllFiltersNullTest() {
        var userAccessCriteria = new UserAccessCriteria();
        assertThat(userAccessCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void userAccessCriteriaFluentMethodsCreatesFiltersTest() {
        var userAccessCriteria = new UserAccessCriteria();

        setAllFilters(userAccessCriteria);

        assertThat(userAccessCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void userAccessCriteriaCopyCreatesNullFilterTest() {
        var userAccessCriteria = new UserAccessCriteria();
        var copy = userAccessCriteria.copy();

        assertThat(userAccessCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(userAccessCriteria)
        );
    }

    @Test
    void userAccessCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var userAccessCriteria = new UserAccessCriteria();
        setAllFilters(userAccessCriteria);

        var copy = userAccessCriteria.copy();

        assertThat(userAccessCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(userAccessCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var userAccessCriteria = new UserAccessCriteria();

        assertThat(userAccessCriteria).hasToString("UserAccessCriteria{}");
    }

    private static void setAllFilters(UserAccessCriteria userAccessCriteria) {
        userAccessCriteria.id();
        userAccessCriteria.login();
        userAccessCriteria.ipAddress();
        userAccessCriteria.device();
        userAccessCriteria.createdDate();
        userAccessCriteria.distinct();
    }

    private static Condition<UserAccessCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getLogin()) &&
                condition.apply(criteria.getIpAddress()) &&
                condition.apply(criteria.getDevice()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<UserAccessCriteria> copyFiltersAre(UserAccessCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getLogin(), copy.getLogin()) &&
                condition.apply(criteria.getIpAddress(), copy.getIpAddress()) &&
                condition.apply(criteria.getDevice(), copy.getDevice()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
