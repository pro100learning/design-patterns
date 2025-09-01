package org.codeus.design_patterns.mediator.refactored.dto;

import java.math.BigDecimal;

public record LimitCheckResult(boolean exceedLimits, BigDecimal dailyLimit, BigDecimal currentUsage,
                               String userId, BigDecimal requestedAmount) {
}
