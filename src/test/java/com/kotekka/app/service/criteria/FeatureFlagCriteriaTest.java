package com.kotekka.app.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class FeatureFlagCriteriaTest {

    @Test
    void newFeatureFlagCriteriaHasAllFiltersNullTest() {
        var featureFlagCriteria = new FeatureFlagCriteria();
        assertThat(featureFlagCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void featureFlagCriteriaFluentMethodsCreatesFiltersTest() {
        var featureFlagCriteria = new FeatureFlagCriteria();

        setAllFilters(featureFlagCriteria);

        assertThat(featureFlagCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void featureFlagCriteriaCopyCreatesNullFilterTest() {
        var featureFlagCriteria = new FeatureFlagCriteria();
        var copy = featureFlagCriteria.copy();

        assertThat(featureFlagCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(featureFlagCriteria)
        );
    }

    @Test
    void featureFlagCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var featureFlagCriteria = new FeatureFlagCriteria();
        setAllFilters(featureFlagCriteria);

        var copy = featureFlagCriteria.copy();

        assertThat(featureFlagCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(featureFlagCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var featureFlagCriteria = new FeatureFlagCriteria();

        assertThat(featureFlagCriteria).hasToString("FeatureFlagCriteria{}");
    }

    private static void setAllFilters(FeatureFlagCriteria featureFlagCriteria) {
        featureFlagCriteria.id();
        featureFlagCriteria.name();
        featureFlagCriteria.enabled();
        featureFlagCriteria.description();
        featureFlagCriteria.createdBy();
        featureFlagCriteria.createdDate();
        featureFlagCriteria.updatedBy();
        featureFlagCriteria.updatedDate();
        featureFlagCriteria.distinct();
    }

    private static Condition<FeatureFlagCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getEnabled()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getUpdatedBy()) &&
                condition.apply(criteria.getUpdatedDate()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<FeatureFlagCriteria> copyFiltersAre(FeatureFlagCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getEnabled(), copy.getEnabled()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getUpdatedBy(), copy.getUpdatedBy()) &&
                condition.apply(criteria.getUpdatedDate(), copy.getUpdatedDate()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
