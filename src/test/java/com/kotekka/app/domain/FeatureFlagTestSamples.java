package com.kotekka.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class FeatureFlagTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static FeatureFlag getFeatureFlagSample1() {
        return new FeatureFlag().id(1L).name("name1").description("description1").createdBy("createdBy1").updatedBy("updatedBy1");
    }

    public static FeatureFlag getFeatureFlagSample2() {
        return new FeatureFlag().id(2L).name("name2").description("description2").createdBy("createdBy2").updatedBy("updatedBy2");
    }

    public static FeatureFlag getFeatureFlagRandomSampleGenerator() {
        return new FeatureFlag()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .updatedBy(UUID.randomUUID().toString());
    }
}
