package com.kotekka.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DeviceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Device getDeviceSample1() {
        return new Device()
            .id(1L)
            .uuid(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .deviceUuid("deviceUuid1")
            .type("type1")
            .manufacturer("manufacturer1")
            .model("model1")
            .os("os1")
            .appVersion("appVersion1")
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static Device getDeviceSample2() {
        return new Device()
            .id(2L)
            .uuid(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .deviceUuid("deviceUuid2")
            .type("type2")
            .manufacturer("manufacturer2")
            .model("model2")
            .os("os2")
            .appVersion("appVersion2")
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static Device getDeviceRandomSampleGenerator() {
        return new Device()
            .id(longCount.incrementAndGet())
            .uuid(UUID.randomUUID())
            .deviceUuid(UUID.randomUUID().toString())
            .type(UUID.randomUUID().toString())
            .manufacturer(UUID.randomUUID().toString())
            .model(UUID.randomUUID().toString())
            .os(UUID.randomUUID().toString())
            .appVersion(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
