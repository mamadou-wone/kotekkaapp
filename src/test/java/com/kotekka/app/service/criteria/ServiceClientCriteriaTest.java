package com.kotekka.app.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ServiceClientCriteriaTest {

    @Test
    void newServiceClientCriteriaHasAllFiltersNullTest() {
        var serviceClientCriteria = new ServiceClientCriteria();
        assertThat(serviceClientCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void serviceClientCriteriaFluentMethodsCreatesFiltersTest() {
        var serviceClientCriteria = new ServiceClientCriteria();

        setAllFilters(serviceClientCriteria);

        assertThat(serviceClientCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void serviceClientCriteriaCopyCreatesNullFilterTest() {
        var serviceClientCriteria = new ServiceClientCriteria();
        var copy = serviceClientCriteria.copy();

        assertThat(serviceClientCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(serviceClientCriteria)
        );
    }

    @Test
    void serviceClientCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var serviceClientCriteria = new ServiceClientCriteria();
        setAllFilters(serviceClientCriteria);

        var copy = serviceClientCriteria.copy();

        assertThat(serviceClientCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(serviceClientCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var serviceClientCriteria = new ServiceClientCriteria();

        assertThat(serviceClientCriteria).hasToString("ServiceClientCriteria{}");
    }

    private static void setAllFilters(ServiceClientCriteria serviceClientCriteria) {
        serviceClientCriteria.id();
        serviceClientCriteria.clientId();
        serviceClientCriteria.type();
        serviceClientCriteria.apiKey();
        serviceClientCriteria.status();
        serviceClientCriteria.note();
        serviceClientCriteria.createdDate();
        serviceClientCriteria.lastModifiedDate();
        serviceClientCriteria.distinct();
    }

    private static Condition<ServiceClientCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getClientId()) &&
                condition.apply(criteria.getType()) &&
                condition.apply(criteria.getApiKey()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getNote()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ServiceClientCriteria> copyFiltersAre(
        ServiceClientCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getClientId(), copy.getClientId()) &&
                condition.apply(criteria.getType(), copy.getType()) &&
                condition.apply(criteria.getApiKey(), copy.getApiKey()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getNote(), copy.getNote()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
