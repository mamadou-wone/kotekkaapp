package com.kotekka.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CollectionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Collection getCollectionSample1() {
        return new Collection().id(1L).name("name1");
    }

    public static Collection getCollectionSample2() {
        return new Collection().id(2L).name("name2");
    }

    public static Collection getCollectionRandomSampleGenerator() {
        return new Collection().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
