package com.robin_courault.assista_crise.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class OffreTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Offre getOffreSample1() {
        return new Offre().id(1L).quantite(1);
    }

    public static Offre getOffreSample2() {
        return new Offre().id(2L).quantite(2);
    }

    public static Offre getOffreRandomSampleGenerator() {
        return new Offre().id(longCount.incrementAndGet()).quantite(intCount.incrementAndGet());
    }
}
