package com.kotekka.app.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CacheInfoCriteriaTest {

    @Test
    void newCacheInfoCriteriaHasAllFiltersNullTest() {
        var cacheInfoCriteria = new CacheInfoCriteria();
        assertThat(cacheInfoCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void cacheInfoCriteriaFluentMethodsCreatesFiltersTest() {
        var cacheInfoCriteria = new CacheInfoCriteria();

        setAllFilters(cacheInfoCriteria);

        assertThat(cacheInfoCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void cacheInfoCriteriaCopyCreatesNullFilterTest() {
        var cacheInfoCriteria = new CacheInfoCriteria();
        var copy = cacheInfoCriteria.copy();

        assertThat(cacheInfoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(cacheInfoCriteria)
        );
    }

    @Test
    void cacheInfoCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var cacheInfoCriteria = new CacheInfoCriteria();
        setAllFilters(cacheInfoCriteria);

        var copy = cacheInfoCriteria.copy();

        assertThat(cacheInfoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(cacheInfoCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var cacheInfoCriteria = new CacheInfoCriteria();

        assertThat(cacheInfoCriteria).hasToString("CacheInfoCriteria{}");
    }

    private static void setAllFilters(CacheInfoCriteria cacheInfoCriteria) {
        cacheInfoCriteria.id();
        cacheInfoCriteria.walletHolder();
        cacheInfoCriteria.key();
        cacheInfoCriteria.createdDate();
        cacheInfoCriteria.distinct();
    }

    private static Condition<CacheInfoCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getWalletHolder()) &&
                condition.apply(criteria.getKey()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CacheInfoCriteria> copyFiltersAre(CacheInfoCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getWalletHolder(), copy.getWalletHolder()) &&
                condition.apply(criteria.getKey(), copy.getKey()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
