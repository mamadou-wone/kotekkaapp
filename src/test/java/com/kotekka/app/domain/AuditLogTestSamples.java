package com.kotekka.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AuditLogTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AuditLog getAuditLogSample1() {
        return new AuditLog()
            .id(1L)
            .uuid(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .entityName("entityName1")
            .entityId(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .action("action1")
            .performedBy("performedBy1");
    }

    public static AuditLog getAuditLogSample2() {
        return new AuditLog()
            .id(2L)
            .uuid(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .entityName("entityName2")
            .entityId(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .action("action2")
            .performedBy("performedBy2");
    }

    public static AuditLog getAuditLogRandomSampleGenerator() {
        return new AuditLog()
            .id(longCount.incrementAndGet())
            .uuid(UUID.randomUUID())
            .entityName(UUID.randomUUID().toString())
            .entityId(UUID.randomUUID())
            .action(UUID.randomUUID().toString())
            .performedBy(UUID.randomUUID().toString());
    }
}
