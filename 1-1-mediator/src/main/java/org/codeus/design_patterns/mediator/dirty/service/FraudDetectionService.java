package org.codeus.design_patterns.mediator.dirty.service;

import org.codeus.design_patterns.mediator.common.PaymentRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class FraudDetectionService {

  private final RiskAssessmentService riskAssessmentService;
  private final NotificationService notificationService;
  private final AuditService auditService;

  public FraudDetectionService(RiskAssessmentService riskAssessmentService,
                               NotificationService notificationService,
                               AuditService auditService) {
    this.riskAssessmentService = riskAssessmentService;
    this.notificationService = notificationService;
    this.auditService = auditService;
  }

  public boolean checkForFraud(PaymentRequest request) {
    System.out.println("FraudDetectionService: Checking for fraud...");

    // Basic fraud checks
    boolean isSuspicious = request.getAmount().compareTo(new BigDecimal("10000")) > 0;

    if (isSuspicious) {
      int riskScore = riskAssessmentService.calculateRiskScore(request);

      if (riskScore > 80) {
        notificationService.sendFraudAlert(request.getUserId(),
          "High-risk transaction detected");
        auditService.logSecurityEvent("FRAUD_DETECTED",
          request.getUserId(), request.getAmount());
        return true;
      }
    }

    return false;
  }
}
