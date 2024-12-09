package com.kotekka.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ProductTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Product getProductSample1() {
        return new Product()
            .id(1L)
            .uuid(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .walletHolder(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .title("title1")
            .description("description1")
            .inventoryQuantity(1)
            .inventoryLocation("inventoryLocation1");
    }

    public static Product getProductSample2() {
        return new Product()
            .id(2L)
            .uuid(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .walletHolder(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .title("title2")
            .description("description2")
            .inventoryQuantity(2)
            .inventoryLocation("inventoryLocation2");
    }

    public static Product getProductRandomSampleGenerator() {
        return new Product()
            .id(longCount.incrementAndGet())
            .uuid(UUID.randomUUID())
            .walletHolder(UUID.randomUUID())
            .title(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .inventoryQuantity(intCount.incrementAndGet())
            .inventoryLocation(UUID.randomUUID().toString());
    }
}
