package org.codeus.design_patterns.mediator.refactored.service;

import org.codeus.design_patterns.mediator.refactored.service.interfaces.IAuditService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AuditService implements IAuditService {

  @Override
  public void logSecurityEvent(String eventType, String userId, BigDecimal amount, List<String> details) {
    System.out.println("AuditService: Security event - " + eventType +
      " | User: " + userId + " | Amount: " + amount +
      " | Details: " + String.join(", ", details));
  }

  @Override
  public void logLimitViolation(String userId, BigDecimal amount, BigDecimal limit) {
    System.out.println("AuditService: Limit violation - User: " + userId +
      " | Amount: " + amount + " | Limit: " + limit);
  }

  @Override
  public void logRiskAssessment(String userId, int riskScore) {
    System.out.println("AuditService: Risk assessment - User: " + userId + " | Score: " + riskScore);
  }

  @Override
  public void logPaymentEvent(String eventType, String userId, String transactionId) {
    System.out.println("AuditService: Payment event - " + eventType +
      " | User: " + userId + " | Transaction: " + transactionId);
  }
}
