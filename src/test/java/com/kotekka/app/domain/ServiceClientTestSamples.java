package com.kotekka.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ServiceClientTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ServiceClient getServiceClientSample1() {
        return new ServiceClient()
            .id(1L)
            .clientId(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .apiKey(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .note("note1");
    }

    public static ServiceClient getServiceClientSample2() {
        return new ServiceClient()
            .id(2L)
            .clientId(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .apiKey(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .note("note2");
    }

    public static ServiceClient getServiceClientRandomSampleGenerator() {
        return new ServiceClient()
            .id(longCount.incrementAndGet())
            .clientId(UUID.randomUUID())
            .apiKey(UUID.randomUUID())
            .note(UUID.randomUUID().toString());
    }
}
