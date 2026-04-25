package com.robin_courault.assista_crise.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ModerationActionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static ModerationAction getModerationActionSample1() {
        return new ModerationAction().id(1L).motif("motif1");
    }

    public static ModerationAction getModerationActionSample2() {
        return new ModerationAction().id(2L).motif("motif2");
    }

    public static ModerationAction getModerationActionRandomSampleGenerator() {
        return new ModerationAction().id(longCount.incrementAndGet()).motif(UUID.randomUUID().toString());
    }
}
