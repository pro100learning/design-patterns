package org.codeus.design_patterns.mediator.refactored.payment;

import org.codeus.design_patterns.mediator.common.PaymentRequest;
import org.codeus.design_patterns.mediator.refactored.dto.FraudCheckResult;
import org.codeus.design_patterns.mediator.refactored.dto.LimitCheckResult;
import org.codeus.design_patterns.mediator.refactored.dto.ValidationResult;
import org.codeus.design_patterns.mediator.refactored.payment.interfaces.IPaymentValidationMediator;
import org.codeus.design_patterns.mediator.refactored.service.interfaces.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PaymentValidationMediator implements IPaymentValidationMediator {

  private final IPaymentDetailsValidationService paymentDetailsCheckService;
  private final IFraudDetectionService fraudDetectionService;
  private final ILimitCheckService limitCheckService;
  private final INotificationService notificationService;
  private final IAuditService auditService;

  public PaymentValidationMediator(IPaymentDetailsValidationService paymentDetailsCheckService, IFraudDetectionService fraudDetectionService, ILimitCheckService limitCheckService, INotificationService notificationService, IAuditService auditService) {
    this.paymentDetailsCheckService = paymentDetailsCheckService;
    this.fraudDetectionService = fraudDetectionService;
    this.limitCheckService = limitCheckService;
    this.notificationService = notificationService;
    this.auditService = auditService;
  }

  @Override
  public ValidationResult validate(PaymentRequest request) {
    List<String> errors = paymentDetailsCheckService.validate(request).errors();

    // Check limits
    LimitCheckResult limitResult = limitCheckService.checkDailyLimit(
      request.getUserId(), request.getAmount());

    if (limitResult.exceedLimits()) {
      errors.add("Daily limit exceeded");
      handleLimitViolation(limitResult);
    }

    // Check fraud
    FraudCheckResult fraudResult = fraudDetectionService.checkForFraud(request);

    if (fraudResult.isFraudulent()) {
      errors.add("Transaction flagged as fraudulent");
      handleFraudDetection(fraudResult);
    }

    return new ValidationResult(errors, fraudResult, limitResult);
  }

  private void handleLimitViolation(LimitCheckResult limitResult) {
    // Centralized orchestration of limit violation handling
    auditService.logLimitViolation(limitResult.userId(),
      limitResult.requestedAmount(), limitResult.dailyLimit());

    notificationService.sendLimitExceededNotification(limitResult.userId(),
      limitResult.dailyLimit(), limitResult.requestedAmount());
  }

  private void handleFraudDetection(FraudCheckResult fraudResult) {
    // Centralized orchestration of fraud handling
    auditService.logSecurityEvent("FRAUD_DETECTED", fraudResult.userId(),
      fraudResult.amount(), fraudResult.fraudIndicators());

    notificationService.sendFraudAlert(fraudResult.userId(),
      "High-risk transaction detected", fraudResult.fraudIndicators());
  }
}
