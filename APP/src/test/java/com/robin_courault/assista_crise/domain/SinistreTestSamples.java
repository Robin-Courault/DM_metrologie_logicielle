package com.robin_courault.assista_crise.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class SinistreTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static Sinistre getSinistreSample1() {
        return new Sinistre().id(1L);
    }

    public static Sinistre getSinistreSample2() {
        return new Sinistre().id(2L);
    }

    public static Sinistre getSinistreRandomSampleGenerator() {
        return new Sinistre().id(longCount.incrementAndGet());
    }
}
