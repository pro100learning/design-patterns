package org.codeus.design_patterns.mediator.refactored.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public record FraudCheckResult(boolean isFraudulent, int riskScore, List<String> fraudIndicators, String userId,
                               BigDecimal amount) {
    public FraudCheckResult(boolean isFraudulent, int riskScore, List<String> fraudIndicators,
                            String userId, BigDecimal amount) {
        this.isFraudulent = isFraudulent;
        this.riskScore = riskScore;
        this.fraudIndicators = new ArrayList<>(fraudIndicators);
        this.userId = userId;
        this.amount = amount;
    }

    @Override
    public List<String> fraudIndicators() {
        return new ArrayList<>(fraudIndicators);
    }
}
