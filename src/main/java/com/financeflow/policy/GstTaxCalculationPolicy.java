package com.financeflow.policy;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * GST split calculation policy (CGST + SGST or IGST) with half-even penny rounding.
 */
public class GstTaxCalculationPolicy {
    private static final RoundingMode ROUNDING = RoundingMode.HALF_EVEN;

    public record GstBreakdown(BigDecimal taxableAmount, BigDecimal cgst, BigDecimal sgst, BigDecimal total) {}

    public static GstBreakdown calculateIntraStateGst(BigDecimal taxableAmount, double totalGstRatePercent) {
        if (taxableAmount == null || taxableAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Taxable amount cannot be negative");
        }
        BigDecimal halfRate = BigDecimal.valueOf(totalGstRatePercent / 200.0);
        BigDecimal cgst = taxableAmount.multiply(halfRate).setScale(2, ROUNDING);
        BigDecimal sgst = taxableAmount.multiply(halfRate).setScale(2, ROUNDING);
        BigDecimal total = taxableAmount.add(cgst).add(sgst);

        return new GstBreakdown(taxableAmount.setScale(2, ROUNDING), cgst, sgst, total);
    }
}
