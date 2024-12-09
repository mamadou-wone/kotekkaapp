package com.kotekka.app.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class DiscountCriteriaTest {

    @Test
    void newDiscountCriteriaHasAllFiltersNullTest() {
        var discountCriteria = new DiscountCriteria();
        assertThat(discountCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void discountCriteriaFluentMethodsCreatesFiltersTest() {
        var discountCriteria = new DiscountCriteria();

        setAllFilters(discountCriteria);

        assertThat(discountCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void discountCriteriaCopyCreatesNullFilterTest() {
        var discountCriteria = new DiscountCriteria();
        var copy = discountCriteria.copy();

        assertThat(discountCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(discountCriteria)
        );
    }

    @Test
    void discountCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var discountCriteria = new DiscountCriteria();
        setAllFilters(discountCriteria);

        var copy = discountCriteria.copy();

        assertThat(discountCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(discountCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var discountCriteria = new DiscountCriteria();

        assertThat(discountCriteria).hasToString("DiscountCriteria{}");
    }

    private static void setAllFilters(DiscountCriteria discountCriteria) {
        discountCriteria.id();
        discountCriteria.uuid();
        discountCriteria.name();
        discountCriteria.type();
        discountCriteria.value();
        discountCriteria.startDate();
        discountCriteria.endDate();
        discountCriteria.status();
        discountCriteria.createdBy();
        discountCriteria.createdDate();
        discountCriteria.lastModifiedBy();
        discountCriteria.lastModifiedDate();
        discountCriteria.distinct();
    }

    private static Condition<DiscountCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getUuid()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getType()) &&
                condition.apply(criteria.getValue()) &&
                condition.apply(criteria.getStartDate()) &&
                condition.apply(criteria.getEndDate()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<DiscountCriteria> copyFiltersAre(DiscountCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getUuid(), copy.getUuid()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getType(), copy.getType()) &&
                condition.apply(criteria.getValue(), copy.getValue()) &&
                condition.apply(criteria.getStartDate(), copy.getStartDate()) &&
                condition.apply(criteria.getEndDate(), copy.getEndDate()) &&
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
