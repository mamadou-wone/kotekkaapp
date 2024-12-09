package com.kotekka.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CartTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Cart getCartSample1() {
        return new Cart()
            .id(1L)
            .uuid(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .walletHolder(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .totalQuantity(1)
            .currency("currency1");
    }

    public static Cart getCartSample2() {
        return new Cart()
            .id(2L)
            .uuid(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .walletHolder(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .totalQuantity(2)
            .currency("currency2");
    }

    public static Cart getCartRandomSampleGenerator() {
        return new Cart()
            .id(longCount.incrementAndGet())
            .uuid(UUID.randomUUID())
            .walletHolder(UUID.randomUUID())
            .totalQuantity(intCount.incrementAndGet())
            .currency(UUID.randomUUID().toString());
    }
}
