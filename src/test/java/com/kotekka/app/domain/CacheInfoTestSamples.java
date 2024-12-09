package com.kotekka.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CacheInfoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CacheInfo getCacheInfoSample1() {
        return new CacheInfo().id(1L).walletHolder(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa")).key("key1");
    }

    public static CacheInfo getCacheInfoSample2() {
        return new CacheInfo().id(2L).walletHolder(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367")).key("key2");
    }

    public static CacheInfo getCacheInfoRandomSampleGenerator() {
        return new CacheInfo().id(longCount.incrementAndGet()).walletHolder(UUID.randomUUID()).key(UUID.randomUUID().toString());
    }
}
