package com.robin_courault.assista_crise.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AutoriteTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static Autorite getAutoriteSample1() {
        return new Autorite().id(1L).nom("nom1").territoire("territoire1").contact("contact1");
    }

    public static Autorite getAutoriteSample2() {
        return new Autorite().id(2L).nom("nom2").territoire("territoire2").contact("contact2");
    }

    public static Autorite getAutoriteRandomSampleGenerator() {
        return new Autorite()
            .id(longCount.incrementAndGet())
            .nom(UUID.randomUUID().toString())
            .territoire(UUID.randomUUID().toString())
            .contact(UUID.randomUUID().toString());
    }
}
