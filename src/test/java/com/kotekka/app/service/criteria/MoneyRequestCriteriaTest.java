package com.kotekka.app.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class MoneyRequestCriteriaTest {

    @Test
    void newMoneyRequestCriteriaHasAllFiltersNullTest() {
        var moneyRequestCriteria = new MoneyRequestCriteria();
        assertThat(moneyRequestCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void moneyRequestCriteriaFluentMethodsCreatesFiltersTest() {
        var moneyRequestCriteria = new MoneyRequestCriteria();

        setAllFilters(moneyRequestCriteria);

        assertThat(moneyRequestCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void moneyRequestCriteriaCopyCreatesNullFilterTest() {
        var moneyRequestCriteria = new MoneyRequestCriteria();
        var copy = moneyRequestCriteria.copy();

        assertThat(moneyRequestCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(moneyRequestCriteria)
        );
    }

    @Test
    void moneyRequestCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var moneyRequestCriteria = new MoneyRequestCriteria();
        setAllFilters(moneyRequestCriteria);

        var copy = moneyRequestCriteria.copy();

        assertThat(moneyRequestCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(moneyRequestCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var moneyRequestCriteria = new MoneyRequestCriteria();

        assertThat(moneyRequestCriteria).hasToString("MoneyRequestCriteria{}");
    }

    private static void setAllFilters(MoneyRequestCriteria moneyRequestCriteria) {
        moneyRequestCriteria.id();
        moneyRequestCriteria.uuid();
        moneyRequestCriteria.status();
        moneyRequestCriteria.otherHolder();
        moneyRequestCriteria.amount();
        moneyRequestCriteria.description();
        moneyRequestCriteria.currency();
        moneyRequestCriteria.walletHolder();
        moneyRequestCriteria.createdBy();
        moneyRequestCriteria.createdDate();
        moneyRequestCriteria.lastModifiedBy();
        moneyRequestCriteria.lastModifiedDate();
        moneyRequestCriteria.distinct();
    }

    private static Condition<MoneyRequestCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getUuid()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getOtherHolder()) &&
                condition.apply(criteria.getAmount()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getCurrency()) &&
                condition.apply(criteria.getWalletHolder()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<MoneyRequestCriteria> copyFiltersAre(
        MoneyRequestCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getUuid(), copy.getUuid()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getOtherHolder(), copy.getOtherHolder()) &&
                condition.apply(criteria.getAmount(), copy.getAmount()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getCurrency(), copy.getCurrency()) &&
                condition.apply(criteria.getWalletHolder(), copy.getWalletHolder()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy(), copy.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
