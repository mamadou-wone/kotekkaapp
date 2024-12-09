package com.kotekka.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ReviewTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Review getReviewSample1() {
        return new Review()
            .id(1L)
            .uuid(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .product(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .walletHolder(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .rating(1)
            .comment("comment1");
    }

    public static Review getReviewSample2() {
        return new Review()
            .id(2L)
            .uuid(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .product(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .walletHolder(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .rating(2)
            .comment("comment2");
    }

    public static Review getReviewRandomSampleGenerator() {
        return new Review()
            .id(longCount.incrementAndGet())
            .uuid(UUID.randomUUID())
            .product(UUID.randomUUID())
            .walletHolder(UUID.randomUUID())
            .rating(intCount.incrementAndGet())
            .comment(UUID.randomUUID().toString());
    }
}
