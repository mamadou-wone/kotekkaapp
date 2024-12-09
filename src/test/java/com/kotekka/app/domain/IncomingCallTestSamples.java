package com.kotekka.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class IncomingCallTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static IncomingCall getIncomingCallSample1() {
        return new IncomingCall().id(1L).api("api1").responseStatusCode(1);
    }

    public static IncomingCall getIncomingCallSample2() {
        return new IncomingCall().id(2L).api("api2").responseStatusCode(2);
    }

    public static IncomingCall getIncomingCallRandomSampleGenerator() {
        return new IncomingCall()
            .id(longCount.incrementAndGet())
            .api(UUID.randomUUID().toString())
            .responseStatusCode(intCount.incrementAndGet());
    }
}
