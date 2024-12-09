package com.kotekka.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CardTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Card getCardSample1() {
        return new Card()
            .id(1L)
            .uuid(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .label("label1")
            .maskedPan("maskedPan1")
            .cardHolderName("cardHolderName1")
            .token("token1")
            .expiryYear("expiryYear1")
            .expiryMonth("expiryMonth1")
            .rnd("rnd1")
            .hash("hash1")
            .walletHolder(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static Card getCardSample2() {
        return new Card()
            .id(2L)
            .uuid(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .label("label2")
            .maskedPan("maskedPan2")
            .cardHolderName("cardHolderName2")
            .token("token2")
            .expiryYear("expiryYear2")
            .expiryMonth("expiryMonth2")
            .rnd("rnd2")
            .hash("hash2")
            .walletHolder(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static Card getCardRandomSampleGenerator() {
        return new Card()
            .id(longCount.incrementAndGet())
            .uuid(UUID.randomUUID())
            .label(UUID.randomUUID().toString())
            .maskedPan(UUID.randomUUID().toString())
            .cardHolderName(UUID.randomUUID().toString())
            .token(UUID.randomUUID().toString())
            .expiryYear(UUID.randomUUID().toString())
            .expiryMonth(UUID.randomUUID().toString())
            .rnd(UUID.randomUUID().toString())
            .hash(UUID.randomUUID().toString())
            .walletHolder(UUID.randomUUID())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
