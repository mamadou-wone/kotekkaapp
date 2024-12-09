package com.kotekka.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class WalletTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Wallet getWalletSample1() {
        return new Wallet()
            .id(1L)
            .uuid(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .iban("iban1")
            .currency("currency1")
            .externalId("externalId1")
            .walletHolder(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static Wallet getWalletSample2() {
        return new Wallet()
            .id(2L)
            .uuid(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .iban("iban2")
            .currency("currency2")
            .externalId("externalId2")
            .walletHolder(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static Wallet getWalletRandomSampleGenerator() {
        return new Wallet()
            .id(longCount.incrementAndGet())
            .uuid(UUID.randomUUID())
            .iban(UUID.randomUUID().toString())
            .currency(UUID.randomUUID().toString())
            .externalId(UUID.randomUUID().toString())
            .walletHolder(UUID.randomUUID())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
