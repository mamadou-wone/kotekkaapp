package com.kotekka.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CinTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Cin getCinSample1() {
        return new Cin()
            .id(1L)
            .cinId("cinId1")
            .birthPlace("birthPlace1")
            .firstName("firstName1")
            .lastName("lastName1")
            .birthCity("birthCity1")
            .fatherName("fatherName1")
            .nationality("nationality1")
            .nationalityCode("nationalityCode1")
            .issuingCountry("issuingCountry1")
            .issuingCountryCode("issuingCountryCode1")
            .motherName("motherName1")
            .civilRegister("civilRegister1")
            .sex("sex1")
            .address("address1")
            .birthCityCode("birthCityCode1")
            .walletHolder(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"));
    }

    public static Cin getCinSample2() {
        return new Cin()
            .id(2L)
            .cinId("cinId2")
            .birthPlace("birthPlace2")
            .firstName("firstName2")
            .lastName("lastName2")
            .birthCity("birthCity2")
            .fatherName("fatherName2")
            .nationality("nationality2")
            .nationalityCode("nationalityCode2")
            .issuingCountry("issuingCountry2")
            .issuingCountryCode("issuingCountryCode2")
            .motherName("motherName2")
            .civilRegister("civilRegister2")
            .sex("sex2")
            .address("address2")
            .birthCityCode("birthCityCode2")
            .walletHolder(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"));
    }

    public static Cin getCinRandomSampleGenerator() {
        return new Cin()
            .id(longCount.incrementAndGet())
            .cinId(UUID.randomUUID().toString())
            .birthPlace(UUID.randomUUID().toString())
            .firstName(UUID.randomUUID().toString())
            .lastName(UUID.randomUUID().toString())
            .birthCity(UUID.randomUUID().toString())
            .fatherName(UUID.randomUUID().toString())
            .nationality(UUID.randomUUID().toString())
            .nationalityCode(UUID.randomUUID().toString())
            .issuingCountry(UUID.randomUUID().toString())
            .issuingCountryCode(UUID.randomUUID().toString())
            .motherName(UUID.randomUUID().toString())
            .civilRegister(UUID.randomUUID().toString())
            .sex(UUID.randomUUID().toString())
            .address(UUID.randomUUID().toString())
            .birthCityCode(UUID.randomUUID().toString())
            .walletHolder(UUID.randomUUID());
    }
}
