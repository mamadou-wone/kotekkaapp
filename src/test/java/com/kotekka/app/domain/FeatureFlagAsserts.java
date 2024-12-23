package com.kotekka.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class FeatureFlagAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertFeatureFlagAllPropertiesEquals(FeatureFlag expected, FeatureFlag actual) {
        assertFeatureFlagAutoGeneratedPropertiesEquals(expected, actual);
        assertFeatureFlagAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertFeatureFlagAllUpdatablePropertiesEquals(FeatureFlag expected, FeatureFlag actual) {
        assertFeatureFlagUpdatableFieldsEquals(expected, actual);
        assertFeatureFlagUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertFeatureFlagAutoGeneratedPropertiesEquals(FeatureFlag expected, FeatureFlag actual) {
        assertThat(expected)
            .as("Verify FeatureFlag auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertFeatureFlagUpdatableFieldsEquals(FeatureFlag expected, FeatureFlag actual) {
        assertThat(expected)
            .as("Verify FeatureFlag relevant properties")
            .satisfies(e -> assertThat(e.getName()).as("check name").isEqualTo(actual.getName()))
            .satisfies(e -> assertThat(e.getEnabled()).as("check enabled").isEqualTo(actual.getEnabled()))
            .satisfies(e -> assertThat(e.getDescription()).as("check description").isEqualTo(actual.getDescription()))
            .satisfies(e -> assertThat(e.getCreatedBy()).as("check createdBy").isEqualTo(actual.getCreatedBy()))
            .satisfies(e -> assertThat(e.getCreatedDate()).as("check createdDate").isEqualTo(actual.getCreatedDate()))
            .satisfies(e -> assertThat(e.getUpdatedBy()).as("check updatedBy").isEqualTo(actual.getUpdatedBy()))
            .satisfies(e -> assertThat(e.getUpdatedDate()).as("check updatedDate").isEqualTo(actual.getUpdatedDate()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertFeatureFlagUpdatableRelationshipsEquals(FeatureFlag expected, FeatureFlag actual) {
        // empty method
    }
}
