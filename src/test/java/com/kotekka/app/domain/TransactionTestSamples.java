package com.kotekka.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TransactionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Transaction getTransactionSample1() {
        return new Transaction()
            .id(1L)
            .uuid(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .description("description1")
            .currency("currency1")
            .counterpartyId("counterpartyId1")
            .parent(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .reference("reference1")
            .externalId("externalId1")
            .partnerToken("partnerToken1")
            .wallet(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static Transaction getTransactionSample2() {
        return new Transaction()
            .id(2L)
            .uuid(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .description("description2")
            .currency("currency2")
            .counterpartyId("counterpartyId2")
            .parent(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .reference("reference2")
            .externalId("externalId2")
            .partnerToken("partnerToken2")
            .wallet(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static Transaction getTransactionRandomSampleGenerator() {
        return new Transaction()
            .id(longCount.incrementAndGet())
            .uuid(UUID.randomUUID())
            .description(UUID.randomUUID().toString())
            .currency(UUID.randomUUID().toString())
            .counterpartyId(UUID.randomUUID().toString())
            .parent(UUID.randomUUID())
            .reference(UUID.randomUUID().toString())
            .externalId(UUID.randomUUID().toString())
            .partnerToken(UUID.randomUUID().toString())
            .wallet(UUID.randomUUID())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
