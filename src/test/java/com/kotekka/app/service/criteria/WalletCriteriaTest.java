package com.kotekka.app.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class WalletCriteriaTest {

    @Test
    void newWalletCriteriaHasAllFiltersNullTest() {
        var walletCriteria = new WalletCriteria();
        assertThat(walletCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void walletCriteriaFluentMethodsCreatesFiltersTest() {
        var walletCriteria = new WalletCriteria();

        setAllFilters(walletCriteria);

        assertThat(walletCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void walletCriteriaCopyCreatesNullFilterTest() {
        var walletCriteria = new WalletCriteria();
        var copy = walletCriteria.copy();

        assertThat(walletCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(walletCriteria)
        );
    }

    @Test
    void walletCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var walletCriteria = new WalletCriteria();
        setAllFilters(walletCriteria);

        var copy = walletCriteria.copy();

        assertThat(walletCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(walletCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var walletCriteria = new WalletCriteria();

        assertThat(walletCriteria).hasToString("WalletCriteria{}");
    }

    private static void setAllFilters(WalletCriteria walletCriteria) {
        walletCriteria.id();
        walletCriteria.uuid();
        walletCriteria.type();
        walletCriteria.status();
        walletCriteria.level();
        walletCriteria.iban();
        walletCriteria.currency();
        walletCriteria.balance();
        walletCriteria.balancesAsOf();
        walletCriteria.externalId();
        walletCriteria.walletHolder();
        walletCriteria.createdBy();
        walletCriteria.createdDate();
        walletCriteria.lastModifiedBy();
        walletCriteria.lastModifiedDate();
        walletCriteria.distinct();
    }

    private static Condition<WalletCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getUuid()) &&
                condition.apply(criteria.getType()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getLevel()) &&
                condition.apply(criteria.getIban()) &&
                condition.apply(criteria.getCurrency()) &&
                condition.apply(criteria.getBalance()) &&
                condition.apply(criteria.getBalancesAsOf()) &&
                condition.apply(criteria.getExternalId()) &&
                condition.apply(criteria.getWalletHolder()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<WalletCriteria> copyFiltersAre(WalletCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getUuid(), copy.getUuid()) &&
                condition.apply(criteria.getType(), copy.getType()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getLevel(), copy.getLevel()) &&
                condition.apply(criteria.getIban(), copy.getIban()) &&
                condition.apply(criteria.getCurrency(), copy.getCurrency()) &&
                condition.apply(criteria.getBalance(), copy.getBalance()) &&
                condition.apply(criteria.getBalancesAsOf(), copy.getBalancesAsOf()) &&
                condition.apply(criteria.getExternalId(), copy.getExternalId()) &&
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
