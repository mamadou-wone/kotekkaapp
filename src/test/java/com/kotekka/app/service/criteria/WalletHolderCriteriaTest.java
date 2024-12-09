package com.kotekka.app.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class WalletHolderCriteriaTest {

    @Test
    void newWalletHolderCriteriaHasAllFiltersNullTest() {
        var walletHolderCriteria = new WalletHolderCriteria();
        assertThat(walletHolderCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void walletHolderCriteriaFluentMethodsCreatesFiltersTest() {
        var walletHolderCriteria = new WalletHolderCriteria();

        setAllFilters(walletHolderCriteria);

        assertThat(walletHolderCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void walletHolderCriteriaCopyCreatesNullFilterTest() {
        var walletHolderCriteria = new WalletHolderCriteria();
        var copy = walletHolderCriteria.copy();

        assertThat(walletHolderCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(walletHolderCriteria)
        );
    }

    @Test
    void walletHolderCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var walletHolderCriteria = new WalletHolderCriteria();
        setAllFilters(walletHolderCriteria);

        var copy = walletHolderCriteria.copy();

        assertThat(walletHolderCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(walletHolderCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var walletHolderCriteria = new WalletHolderCriteria();

        assertThat(walletHolderCriteria).hasToString("WalletHolderCriteria{}");
    }

    private static void setAllFilters(WalletHolderCriteria walletHolderCriteria) {
        walletHolderCriteria.id();
        walletHolderCriteria.uuid();
        walletHolderCriteria.type();
        walletHolderCriteria.status();
        walletHolderCriteria.phoneNumber();
        walletHolderCriteria.network();
        walletHolderCriteria.tag();
        walletHolderCriteria.firstName();
        walletHolderCriteria.lastName();
        walletHolderCriteria.address();
        walletHolderCriteria.city();
        walletHolderCriteria.country();
        walletHolderCriteria.postalCode();
        walletHolderCriteria.onboarding();
        walletHolderCriteria.externalId();
        walletHolderCriteria.email();
        walletHolderCriteria.dateOfBirth();
        walletHolderCriteria.sex();
        walletHolderCriteria.createdBy();
        walletHolderCriteria.createdDate();
        walletHolderCriteria.lastModifiedBy();
        walletHolderCriteria.lastModifiedDate();
        walletHolderCriteria.loginStatus();
        walletHolderCriteria.userId();
        walletHolderCriteria.distinct();
    }

    private static Condition<WalletHolderCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getUuid()) &&
                condition.apply(criteria.getType()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getPhoneNumber()) &&
                condition.apply(criteria.getNetwork()) &&
                condition.apply(criteria.getTag()) &&
                condition.apply(criteria.getFirstName()) &&
                condition.apply(criteria.getLastName()) &&
                condition.apply(criteria.getAddress()) &&
                condition.apply(criteria.getCity()) &&
                condition.apply(criteria.getCountry()) &&
                condition.apply(criteria.getPostalCode()) &&
                condition.apply(criteria.getOnboarding()) &&
                condition.apply(criteria.getExternalId()) &&
                condition.apply(criteria.getEmail()) &&
                condition.apply(criteria.getDateOfBirth()) &&
                condition.apply(criteria.getSex()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getLoginStatus()) &&
                condition.apply(criteria.getUserId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<WalletHolderCriteria> copyFiltersAre(
        WalletHolderCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getUuid(), copy.getUuid()) &&
                condition.apply(criteria.getType(), copy.getType()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getPhoneNumber(), copy.getPhoneNumber()) &&
                condition.apply(criteria.getNetwork(), copy.getNetwork()) &&
                condition.apply(criteria.getTag(), copy.getTag()) &&
                condition.apply(criteria.getFirstName(), copy.getFirstName()) &&
                condition.apply(criteria.getLastName(), copy.getLastName()) &&
                condition.apply(criteria.getAddress(), copy.getAddress()) &&
                condition.apply(criteria.getCity(), copy.getCity()) &&
                condition.apply(criteria.getCountry(), copy.getCountry()) &&
                condition.apply(criteria.getPostalCode(), copy.getPostalCode()) &&
                condition.apply(criteria.getOnboarding(), copy.getOnboarding()) &&
                condition.apply(criteria.getExternalId(), copy.getExternalId()) &&
                condition.apply(criteria.getEmail(), copy.getEmail()) &&
                condition.apply(criteria.getDateOfBirth(), copy.getDateOfBirth()) &&
                condition.apply(criteria.getSex(), copy.getSex()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy(), copy.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getLoginStatus(), copy.getLoginStatus()) &&
                condition.apply(criteria.getUserId(), copy.getUserId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
