package com.kotekka.app.domain;

import static com.kotekka.app.domain.AssertUtils.bigDecimalCompareTo;
import static org.assertj.core.api.Assertions.assertThat;

public class TransactionAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTransactionAllPropertiesEquals(Transaction expected, Transaction actual) {
        assertTransactionAutoGeneratedPropertiesEquals(expected, actual);
        assertTransactionAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTransactionAllUpdatablePropertiesEquals(Transaction expected, Transaction actual) {
        assertTransactionUpdatableFieldsEquals(expected, actual);
        assertTransactionUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTransactionAutoGeneratedPropertiesEquals(Transaction expected, Transaction actual) {
        assertThat(expected)
            .as("Verify Transaction auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTransactionUpdatableFieldsEquals(Transaction expected, Transaction actual) {
        assertThat(expected)
            .as("Verify Transaction relevant properties")
            .satisfies(e -> assertThat(e.getUuid()).as("check uuid").isEqualTo(actual.getUuid()))
            .satisfies(e -> assertThat(e.getType()).as("check type").isEqualTo(actual.getType()))
            .satisfies(e -> assertThat(e.getStatus()).as("check status").isEqualTo(actual.getStatus()))
            .satisfies(e -> assertThat(e.getDirection()).as("check direction").isEqualTo(actual.getDirection()))
            .satisfies(e -> assertThat(e.getAmount()).as("check amount").usingComparator(bigDecimalCompareTo).isEqualTo(actual.getAmount()))
            .satisfies(e -> assertThat(e.getDescription()).as("check description").isEqualTo(actual.getDescription()))
            .satisfies(e -> assertThat(e.getFee()).as("check fee").usingComparator(bigDecimalCompareTo).isEqualTo(actual.getFee()))
            .satisfies(e ->
                assertThat(e.getCommission()).as("check commission").usingComparator(bigDecimalCompareTo).isEqualTo(actual.getCommission())
            )
            .satisfies(e -> assertThat(e.getCurrency()).as("check currency").isEqualTo(actual.getCurrency()))
            .satisfies(e -> assertThat(e.getCounterpartyType()).as("check counterpartyType").isEqualTo(actual.getCounterpartyType()))
            .satisfies(e -> assertThat(e.getCounterpartyId()).as("check counterpartyId").isEqualTo(actual.getCounterpartyId()))
            .satisfies(e -> assertThat(e.getEntryDate()).as("check entryDate").isEqualTo(actual.getEntryDate()))
            .satisfies(e -> assertThat(e.getEffectiveDate()).as("check effectiveDate").isEqualTo(actual.getEffectiveDate()))
            .satisfies(e -> assertThat(e.getStartTime()).as("check startTime").isEqualTo(actual.getStartTime()))
            .satisfies(e -> assertThat(e.getEndTime()).as("check endTime").isEqualTo(actual.getEndTime()))
            .satisfies(e -> assertThat(e.getParent()).as("check parent").isEqualTo(actual.getParent()))
            .satisfies(e -> assertThat(e.getReference()).as("check reference").isEqualTo(actual.getReference()))
            .satisfies(e -> assertThat(e.getExternalId()).as("check externalId").isEqualTo(actual.getExternalId()))
            .satisfies(e -> assertThat(e.getPartnerToken()).as("check partnerToken").isEqualTo(actual.getPartnerToken()))
            .satisfies(e -> assertThat(e.getWallet()).as("check wallet").isEqualTo(actual.getWallet()))
            .satisfies(e -> assertThat(e.getCreatedBy()).as("check createdBy").isEqualTo(actual.getCreatedBy()))
            .satisfies(e -> assertThat(e.getCreatedDate()).as("check createdDate").isEqualTo(actual.getCreatedDate()))
            .satisfies(e -> assertThat(e.getLastModifiedBy()).as("check lastModifiedBy").isEqualTo(actual.getLastModifiedBy()))
            .satisfies(e -> assertThat(e.getLastModifiedDate()).as("check lastModifiedDate").isEqualTo(actual.getLastModifiedDate()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTransactionUpdatableRelationshipsEquals(Transaction expected, Transaction actual) {
        // empty method
    }
}