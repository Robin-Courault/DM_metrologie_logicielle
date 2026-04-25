package com.robin_courault.assista_crise.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class SalonDiscussionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static SalonDiscussion getSalonDiscussionSample1() {
        return new SalonDiscussion().id(1L);
    }

    public static SalonDiscussion getSalonDiscussionSample2() {
        return new SalonDiscussion().id(2L);
    }

    public static SalonDiscussion getSalonDiscussionRandomSampleGenerator() {
        return new SalonDiscussion().id(longCount.incrementAndGet());
    }
}
