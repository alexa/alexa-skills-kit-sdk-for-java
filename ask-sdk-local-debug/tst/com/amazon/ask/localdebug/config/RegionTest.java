package com.amazon.ask.localdebug.config;

import org.junit.Assert;
import org.junit.Test;

public class RegionTest {
    @Test
    public void validateNAEndpoint() {
        Assert.assertEquals("bob-dispatch-prod-na.amazon.com", Region.valueOf("NA").getEndpoint());
    }

    @Test
    public void validateFEEndpoint() {
        Assert.assertEquals("bob-dispatch-prod-fe.amazon.com", Region.valueOf("FE").getEndpoint());
    }

    @Test
    public void validateEUEndpoint() {
        Assert.assertEquals("bob-dispatch-prod-eu.amazon.com", Region.valueOf("EU").getEndpoint());
    }
}
