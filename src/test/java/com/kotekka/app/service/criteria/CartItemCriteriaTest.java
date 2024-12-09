package com.kotekka.app.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CartItemCriteriaTest {

    @Test
    void newCartItemCriteriaHasAllFiltersNullTest() {
        var cartItemCriteria = new CartItemCriteria();
        assertThat(cartItemCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void cartItemCriteriaFluentMethodsCreatesFiltersTest() {
        var cartItemCriteria = new CartItemCriteria();

        setAllFilters(cartItemCriteria);

        assertThat(cartItemCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void cartItemCriteriaCopyCreatesNullFilterTest() {
        var cartItemCriteria = new CartItemCriteria();
        var copy = cartItemCriteria.copy();

        assertThat(cartItemCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(cartItemCriteria)
        );
    }

    @Test
    void cartItemCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var cartItemCriteria = new CartItemCriteria();
        setAllFilters(cartItemCriteria);

        var copy = cartItemCriteria.copy();

        assertThat(cartItemCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(cartItemCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var cartItemCriteria = new CartItemCriteria();

        assertThat(cartItemCriteria).hasToString("CartItemCriteria{}");
    }

    private static void setAllFilters(CartItemCriteria cartItemCriteria) {
        cartItemCriteria.id();
        cartItemCriteria.uuid();
        cartItemCriteria.cart();
        cartItemCriteria.product();
        cartItemCriteria.quantity();
        cartItemCriteria.price();
        cartItemCriteria.totalPrice();
        cartItemCriteria.distinct();
    }

    private static Condition<CartItemCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getUuid()) &&
                condition.apply(criteria.getCart()) &&
                condition.apply(criteria.getProduct()) &&
                condition.apply(criteria.getQuantity()) &&
                condition.apply(criteria.getPrice()) &&
                condition.apply(criteria.getTotalPrice()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CartItemCriteria> copyFiltersAre(CartItemCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getUuid(), copy.getUuid()) &&
                condition.apply(criteria.getCart(), copy.getCart()) &&
                condition.apply(criteria.getProduct(), copy.getProduct()) &&
                condition.apply(criteria.getQuantity(), copy.getQuantity()) &&
                condition.apply(criteria.getPrice(), copy.getPrice()) &&
                condition.apply(criteria.getTotalPrice(), copy.getTotalPrice()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
