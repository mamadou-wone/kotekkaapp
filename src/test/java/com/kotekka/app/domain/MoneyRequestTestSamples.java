package com.kotekka.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MoneyRequestTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static MoneyRequest getMoneyRequestSample1() {
        return new MoneyRequest()
            .id(1L)
            .uuid(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .otherHolder(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .description("description1")
            .currency("currency1")
            .walletHolder(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static MoneyRequest getMoneyRequestSample2() {
        return new MoneyRequest()
            .id(2L)
            .uuid(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .otherHolder(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .description("description2")
            .currency("currency2")
            .walletHolder(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static MoneyRequest getMoneyRequestRandomSampleGenerator() {
        return new MoneyRequest()
            .id(longCount.incrementAndGet())
            .uuid(UUID.randomUUID())
            .otherHolder(UUID.randomUUID())
            .description(UUID.randomUUID().toString())
            .currency(UUID.randomUUID().toString())
            .walletHolder(UUID.randomUUID())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
