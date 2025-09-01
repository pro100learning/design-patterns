package org.codeus.design_patterns.mediator.refactored.service;

import org.codeus.design_patterns.mediator.common.PaymentRequest;
import org.codeus.design_patterns.mediator.refactored.service.interfaces.IAccountService;
import org.codeus.design_patterns.mediator.refactored.service.interfaces.IRiskAssessmentService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class RiskAssessmentService implements IRiskAssessmentService {

  private final IAccountService accountService;

  public RiskAssessmentService(IAccountService accountService) {
    this.accountService = accountService;
  }

  @Override
  public int calculateRiskScore(PaymentRequest request) {
    System.out.println("RiskAssessmentService: Calculating risk score...");

    int riskScore = 0;

    // Risk factors
    if (request.getAmount().compareTo(new BigDecimal("5000")) > 0) {
      riskScore += 30;
    }

    if (accountService.isNewAccount(request.getUserId())) {
      riskScore += 40;
    }

    if ("INTERNATIONAL".equals(request.getPaymentMethod())) {
      riskScore += 25;
    }

    return riskScore;
  }
}
