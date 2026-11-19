package com.financeflow.policy;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

public class GstTaxCalculationPolicyTest {
    @Test
    void test18PercentGstSplit() {
        var breakdown = GstTaxCalculationPolicy.calculateIntraStateGst(new BigDecimal("1000.00"), 18.0);
        assertEquals(new BigDecimal("90.00"), breakdown.cgst());
        assertEquals(new BigDecimal("90.00"), breakdown.sgst());
        assertEquals(new BigDecimal("1180.00"), breakdown.total());
    }
}
