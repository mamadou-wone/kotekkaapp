package com.kotekka.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class NotificationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Notification getNotificationSample1() {
        return new Notification()
            .id(1L)
            .uuid(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .walletHolder(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .heading("heading1")
            .content("content1")
            .data("data1")
            .language("language1");
    }

    public static Notification getNotificationSample2() {
        return new Notification()
            .id(2L)
            .uuid(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .walletHolder(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .heading("heading2")
            .content("content2")
            .data("data2")
            .language("language2");
    }

    public static Notification getNotificationRandomSampleGenerator() {
        return new Notification()
            .id(longCount.incrementAndGet())
            .uuid(UUID.randomUUID())
            .walletHolder(UUID.randomUUID())
            .heading(UUID.randomUUID().toString())
            .content(UUID.randomUUID().toString())
            .data(UUID.randomUUID().toString())
            .language(UUID.randomUUID().toString());
    }
}
