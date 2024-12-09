package com.kotekka.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UserPreferenceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static UserPreference getUserPreferenceSample1() {
        return new UserPreference().id(1L).name("name1").setting("setting1");
    }

    public static UserPreference getUserPreferenceSample2() {
        return new UserPreference().id(2L).name("name2").setting("setting2");
    }

    public static UserPreference getUserPreferenceRandomSampleGenerator() {
        return new UserPreference()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .setting(UUID.randomUUID().toString());
    }
}
