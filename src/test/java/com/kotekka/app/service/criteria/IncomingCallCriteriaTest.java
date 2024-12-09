package com.kotekka.app.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class IncomingCallCriteriaTest {

    @Test
    void newIncomingCallCriteriaHasAllFiltersNullTest() {
        var incomingCallCriteria = new IncomingCallCriteria();
        assertThat(incomingCallCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void incomingCallCriteriaFluentMethodsCreatesFiltersTest() {
        var incomingCallCriteria = new IncomingCallCriteria();

        setAllFilters(incomingCallCriteria);

        assertThat(incomingCallCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void incomingCallCriteriaCopyCreatesNullFilterTest() {
        var incomingCallCriteria = new IncomingCallCriteria();
        var copy = incomingCallCriteria.copy();

        assertThat(incomingCallCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(incomingCallCriteria)
        );
    }

    @Test
    void incomingCallCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var incomingCallCriteria = new IncomingCallCriteria();
        setAllFilters(incomingCallCriteria);

        var copy = incomingCallCriteria.copy();

        assertThat(incomingCallCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(incomingCallCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var incomingCallCriteria = new IncomingCallCriteria();

        assertThat(incomingCallCriteria).hasToString("IncomingCallCriteria{}");
    }

    private static void setAllFilters(IncomingCallCriteria incomingCallCriteria) {
        incomingCallCriteria.id();
        incomingCallCriteria.partner();
        incomingCallCriteria.api();
        incomingCallCriteria.method();
        incomingCallCriteria.createdDate();
        incomingCallCriteria.responseStatusCode();
        incomingCallCriteria.responseTime();
        incomingCallCriteria.distinct();
    }

    private static Condition<IncomingCallCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getPartner()) &&
                condition.apply(criteria.getApi()) &&
                condition.apply(criteria.getMethod()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getResponseStatusCode()) &&
                condition.apply(criteria.getResponseTime()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<IncomingCallCriteria> copyFiltersAre(
        IncomingCallCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getPartner(), copy.getPartner()) &&
                condition.apply(criteria.getApi(), copy.getApi()) &&
                condition.apply(criteria.getMethod(), copy.getMethod()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getResponseStatusCode(), copy.getResponseStatusCode()) &&
                condition.apply(criteria.getResponseTime(), copy.getResponseTime()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
