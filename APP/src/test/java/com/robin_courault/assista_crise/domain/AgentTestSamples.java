package com.robin_courault.assista_crise.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AgentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static Agent getAgentSample1() {
        return new Agent().id(1L).fonction("fonction1").service("service1");
    }

    public static Agent getAgentSample2() {
        return new Agent().id(2L).fonction("fonction2").service("service2");
    }

    public static Agent getAgentRandomSampleGenerator() {
        return new Agent().id(longCount.incrementAndGet()).fonction(UUID.randomUUID().toString()).service(UUID.randomUUID().toString());
    }
}
