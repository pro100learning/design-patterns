package org.codeus.design_patterns.mediator.refactored.service;

import org.codeus.design_patterns.mediator.common.PaymentRequest;
import org.codeus.design_patterns.mediator.refactored.dto.FraudCheckResult;
import org.codeus.design_patterns.mediator.refactored.service.interfaces.IFraudDetectionService;
import org.codeus.design_patterns.mediator.refactored.service.interfaces.IRiskAssessmentService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class FraudDetectionService implements IFraudDetectionService {

  private final IRiskAssessmentService riskAssessmentService;

  public FraudDetectionService(IRiskAssessmentService riskAssessmentService) {
    this.riskAssessmentService = riskAssessmentService;
  }

  @Override
  public FraudCheckResult checkForFraud(PaymentRequest request) {
    System.out.println("FraudDetectionService: Checking for fraud...");

    List<String> fraudIndicators = new ArrayList<>();
    boolean isFraudulent = false;

    // High amount check
    if (request.getAmount().compareTo(new BigDecimal("10000")) > 0) {
      fraudIndicators.add("High transaction amount");
    }

    // Get risk score from risk assessment service
    int riskScore = riskAssessmentService.calculateRiskScore(request);

    if (riskScore > 80) {
      isFraudulent = true;
      fraudIndicators.add("High risk score: " + riskScore);
    }

    // International payment method check
    if ("INTERNATIONAL".equals(request.getPaymentMethod())) {
      fraudIndicators.add("International payment method");
    }

    return new FraudCheckResult(isFraudulent, riskScore, fraudIndicators,
      request.getUserId(), request.getAmount());
  }
}