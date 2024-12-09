package com.kotekka.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class OneTimePasswordTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static OneTimePassword getOneTimePasswordSample1() {
        return new OneTimePassword().id(1L).uuid(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa")).user("user1").code("code1");
    }

    public static OneTimePassword getOneTimePasswordSample2() {
        return new OneTimePassword().id(2L).uuid(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367")).user("user2").code("code2");
    }

    public static OneTimePassword getOneTimePasswordRandomSampleGenerator() {
        return new OneTimePassword()
            .id(longCount.incrementAndGet())
            .uuid(UUID.randomUUID())
            .user(UUID.randomUUID().toString())
            .code(UUID.randomUUID().toString());
    }
}
