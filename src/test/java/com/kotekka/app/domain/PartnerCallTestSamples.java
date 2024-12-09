package com.kotekka.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class PartnerCallTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static PartnerCall getPartnerCallSample1() {
        return new PartnerCall().id(1L).api("api1").responseStatusCode(1).correlationId("correlationId1").queryParam("queryParam1");
    }

    public static PartnerCall getPartnerCallSample2() {
        return new PartnerCall().id(2L).api("api2").responseStatusCode(2).correlationId("correlationId2").queryParam("queryParam2");
    }

    public static PartnerCall getPartnerCallRandomSampleGenerator() {
        return new PartnerCall()
            .id(longCount.incrementAndGet())
            .api(UUID.randomUUID().toString())
            .responseStatusCode(intCount.incrementAndGet())
            .correlationId(UUID.randomUUID().toString())
            .queryParam(UUID.randomUUID().toString());
    }
}
