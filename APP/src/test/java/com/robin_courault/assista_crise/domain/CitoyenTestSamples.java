package com.robin_courault.assista_crise.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class CitoyenTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static Citoyen getCitoyenSample1() {
        return new Citoyen().id(1L);
    }

    public static Citoyen getCitoyenSample2() {
        return new Citoyen().id(2L);
    }

    public static Citoyen getCitoyenRandomSampleGenerator() {
        return new Citoyen().id(longCount.incrementAndGet());
    }
}
