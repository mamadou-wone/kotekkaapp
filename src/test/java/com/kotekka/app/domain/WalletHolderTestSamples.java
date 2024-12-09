package com.kotekka.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class WalletHolderTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static WalletHolder getWalletHolderSample1() {
        return new WalletHolder()
            .id(1L)
            .uuid(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .phoneNumber("phoneNumber1")
            .tag("tag1")
            .firstName("firstName1")
            .lastName("lastName1")
            .address("address1")
            .city("city1")
            .country("country1")
            .postalCode("postalCode1")
            .externalId("externalId1")
            .email("email1")
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static WalletHolder getWalletHolderSample2() {
        return new WalletHolder()
            .id(2L)
            .uuid(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .phoneNumber("phoneNumber2")
            .tag("tag2")
            .firstName("firstName2")
            .lastName("lastName2")
            .address("address2")
            .city("city2")
            .country("country2")
            .postalCode("postalCode2")
            .externalId("externalId2")
            .email("email2")
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static WalletHolder getWalletHolderRandomSampleGenerator() {
        return new WalletHolder()
            .id(longCount.incrementAndGet())
            .uuid(UUID.randomUUID())
            .phoneNumber(UUID.randomUUID().toString())
            .tag(UUID.randomUUID().toString())
            .firstName(UUID.randomUUID().toString())
            .lastName(UUID.randomUUID().toString())
            .address(UUID.randomUUID().toString())
            .city(UUID.randomUUID().toString())
            .country(UUID.randomUUID().toString())
            .postalCode(UUID.randomUUID().toString())
            .externalId(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
