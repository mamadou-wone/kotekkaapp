package com.kotekka.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CartItemTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static CartItem getCartItemSample1() {
        return new CartItem()
            .id(1L)
            .uuid(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .cart(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .product(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .quantity(1);
    }

    public static CartItem getCartItemSample2() {
        return new CartItem()
            .id(2L)
            .uuid(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .cart(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .product(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .quantity(2);
    }

    public static CartItem getCartItemRandomSampleGenerator() {
        return new CartItem()
            .id(longCount.incrementAndGet())
            .uuid(UUID.randomUUID())
            .cart(UUID.randomUUID())
            .product(UUID.randomUUID())
            .quantity(intCount.incrementAndGet());
    }
}
