package io.quarkus.infra.performance.graphics.model;

import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.databind.ObjectMapper;

public record Ec2Pricing(String instanceType, String region, int vCPUs, int memoryGiB,
                          double onDemandPricePerHour, String currency, String lastUpdated) {

    private static final String RESOURCE_PATH = "/ec2-pricing.json";

    public static Ec2Pricing load() {
        try (InputStream is = Ec2Pricing.class.getResourceAsStream(RESOURCE_PATH)) {
            if (is == null) {
                throw new IllegalStateException("Missing classpath resource: " + RESOURCE_PATH);
            }
            return new ObjectMapper().readValue(is, Ec2Pricing.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load EC2 pricing from " + RESOURCE_PATH, e);
        }
    }
}
