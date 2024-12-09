package com.kotekka.app.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PartnerCallCriteriaTest {

    @Test
    void newPartnerCallCriteriaHasAllFiltersNullTest() {
        var partnerCallCriteria = new PartnerCallCriteria();
        assertThat(partnerCallCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void partnerCallCriteriaFluentMethodsCreatesFiltersTest() {
        var partnerCallCriteria = new PartnerCallCriteria();

        setAllFilters(partnerCallCriteria);

        assertThat(partnerCallCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void partnerCallCriteriaCopyCreatesNullFilterTest() {
        var partnerCallCriteria = new PartnerCallCriteria();
        var copy = partnerCallCriteria.copy();

        assertThat(partnerCallCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(partnerCallCriteria)
        );
    }

    @Test
    void partnerCallCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var partnerCallCriteria = new PartnerCallCriteria();
        setAllFilters(partnerCallCriteria);

        var copy = partnerCallCriteria.copy();

        assertThat(partnerCallCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(partnerCallCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var partnerCallCriteria = new PartnerCallCriteria();

        assertThat(partnerCallCriteria).hasToString("PartnerCallCriteria{}");
    }

    private static void setAllFilters(PartnerCallCriteria partnerCallCriteria) {
        partnerCallCriteria.id();
        partnerCallCriteria.partner();
        partnerCallCriteria.api();
        partnerCallCriteria.method();
        partnerCallCriteria.requestTime();
        partnerCallCriteria.responseStatusCode();
        partnerCallCriteria.responseTime();
        partnerCallCriteria.correlationId();
        partnerCallCriteria.queryParam();
        partnerCallCriteria.distinct();
    }

    private static Condition<PartnerCallCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getPartner()) &&
                condition.apply(criteria.getApi()) &&
                condition.apply(criteria.getMethod()) &&
                condition.apply(criteria.getRequestTime()) &&
                condition.apply(criteria.getResponseStatusCode()) &&
                condition.apply(criteria.getResponseTime()) &&
                condition.apply(criteria.getCorrelationId()) &&
                condition.apply(criteria.getQueryParam()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PartnerCallCriteria> copyFiltersAre(PartnerCallCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getPartner(), copy.getPartner()) &&
                condition.apply(criteria.getApi(), copy.getApi()) &&
                condition.apply(criteria.getMethod(), copy.getMethod()) &&
                condition.apply(criteria.getRequestTime(), copy.getRequestTime()) &&
                condition.apply(criteria.getResponseStatusCode(), copy.getResponseStatusCode()) &&
                condition.apply(criteria.getResponseTime(), copy.getResponseTime()) &&
                condition.apply(criteria.getCorrelationId(), copy.getCorrelationId()) &&
                condition.apply(criteria.getQueryParam(), copy.getQueryParam()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
