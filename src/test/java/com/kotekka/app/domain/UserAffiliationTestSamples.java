package com.kotekka.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UserAffiliationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static UserAffiliation getUserAffiliationSample1() {
        return new UserAffiliation()
            .id(1L)
            .walletHolder(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .affiliation("affiliation1")
            .createdBy("createdBy1");
    }

    public static UserAffiliation getUserAffiliationSample2() {
        return new UserAffiliation()
            .id(2L)
            .walletHolder(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .affiliation("affiliation2")
            .createdBy("createdBy2");
    }

    public static UserAffiliation getUserAffiliationRandomSampleGenerator() {
        return new UserAffiliation()
            .id(longCount.incrementAndGet())
            .walletHolder(UUID.randomUUID())
            .affiliation(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString());
    }
}
