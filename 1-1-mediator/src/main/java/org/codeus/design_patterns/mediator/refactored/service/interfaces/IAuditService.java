package org.codeus.design_patterns.mediator.refactored.service.interfaces;

import java.math.BigDecimal;
import java.util.List;

public interface IAuditService {
    void logSecurityEvent(String eventType, String userId, BigDecimal amount, List<String> details);
    void logLimitViolation(String userId, BigDecimal amount, BigDecimal limit);
    void logRiskAssessment(String userId, int riskScore);
    void logPaymentEvent(String eventType, String userId, String transactionId);
}
