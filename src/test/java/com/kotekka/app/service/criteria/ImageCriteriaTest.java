package com.kotekka.app.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ImageCriteriaTest {

    @Test
    void newImageCriteriaHasAllFiltersNullTest() {
        var imageCriteria = new ImageCriteria();
        assertThat(imageCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void imageCriteriaFluentMethodsCreatesFiltersTest() {
        var imageCriteria = new ImageCriteria();

        setAllFilters(imageCriteria);

        assertThat(imageCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void imageCriteriaCopyCreatesNullFilterTest() {
        var imageCriteria = new ImageCriteria();
        var copy = imageCriteria.copy();

        assertThat(imageCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(imageCriteria)
        );
    }

    @Test
    void imageCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var imageCriteria = new ImageCriteria();
        setAllFilters(imageCriteria);

        var copy = imageCriteria.copy();

        assertThat(imageCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(imageCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var imageCriteria = new ImageCriteria();

        assertThat(imageCriteria).hasToString("ImageCriteria{}");
    }

    private static void setAllFilters(ImageCriteria imageCriteria) {
        imageCriteria.id();
        imageCriteria.uuid();
        imageCriteria.name();
        imageCriteria.walletHolder();
        imageCriteria.createdBy();
        imageCriteria.createdDate();
        imageCriteria.lastModifiedBy();
        imageCriteria.lastModifiedDate();
        imageCriteria.distinct();
    }

    private static Condition<ImageCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getUuid()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getWalletHolder()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ImageCriteria> copyFiltersAre(ImageCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getUuid(), copy.getUuid()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
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
