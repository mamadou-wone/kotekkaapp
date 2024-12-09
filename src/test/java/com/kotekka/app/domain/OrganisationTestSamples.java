package com.kotekka.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class OrganisationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Organisation getOrganisationSample1() {
        return new Organisation().id(1L).name("name1").parent("parent1").location("location1").headcount(1).createdBy("createdBy1");
    }

    public static Organisation getOrganisationSample2() {
        return new Organisation().id(2L).name("name2").parent("parent2").location("location2").headcount(2).createdBy("createdBy2");
    }

    public static Organisation getOrganisationRandomSampleGenerator() {
        return new Organisation()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .parent(UUID.randomUUID().toString())
            .location(UUID.randomUUID().toString())
            .headcount(intCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString());
    }
}
