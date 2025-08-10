package com.ozmenyavuz.output;

public record Investment(
        String investmentType,   // e.g., "ETF", "Savings"
        String region,           // e.g., "Europe", "Global"
        String riskLevel,        // e.g., "low", "medium", "high"
        String expectedReturn    // e.g., "5-7% annually"
) {
}
