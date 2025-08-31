package org.codeus.design_patterns.mediator.dirty.payment;

import org.codeus.design_patterns.mediator.common.*;
import org.codeus.design_patterns.mediator.common.interfaces.IPaymentProcessingService;
import org.codeus.design_patterns.mediator.common.util.TransactionCreator;
import org.codeus.design_patterns.mediator.dirty.service.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PaymentProcessingService implements IPaymentProcessingService {

  private final PaymentGatewayService gatewayService;
  private final NotificationService notificationService;
  private final AuditService auditService;
  private final LimitCheckService limitCheckService;
  private final FraudDetectionService fraudDetectionService;

  public PaymentProcessingService(PaymentGatewayService gatewayService,
                                  NotificationService notificationService,
                                  AuditService auditService, LimitCheckService limitCheckService, FraudDetectionService fraudDetectionService) {
    this.gatewayService = gatewayService;
    this.notificationService = notificationService;
    this.auditService = auditService;
    this.limitCheckService = limitCheckService;
    this.fraudDetectionService = fraudDetectionService;
  }

  @Override
  public PaymentProcessingResult processPayment(PaymentRequest request) {
    //***** Validation-specific logic
    System.out.println("PaymentValidationService: Validating payment...");

    // Basic validation
    var isPaymentRequestValid = request.getAmount().compareTo(BigDecimal.ZERO) > 0;

    if (!limitCheckService.checkDailyLimit(request.getUserId(), request.getAmount())) {
      notificationService.sendLimitExceededNotification(request.getUserId());
      isPaymentRequestValid = false;
    }

    if (fraudDetectionService.checkForFraud(request)) {
      isPaymentRequestValid = false;
    }

    System.out.println("PaymentProcessingService: Processing payment...");

    if (!isPaymentRequestValid) {
      Transaction failedTransaction = TransactionCreator.createDeclinedTransaction(request, TransactionStatus.DECLINED);
      notificationService.sendPaymentDeclinedNotification(request.getUserId());
      auditService.logPaymentEvent("PAYMENT_DECLINED", request.getUserId());
      return new PaymentProcessingResult(failedTransaction);
    }
    //***** /Validation-specific logic

    try {
      //***** Payment-specific logic
      GatewayRequest gatewayRequest = createGatewayRequest(request);
      Transaction transaction = gatewayService.processPayment(gatewayRequest);

      if (transaction.isSuccessful()) {
        notificationService.sendPaymentSuccessNotification(request.getUserId());
        auditService.logPaymentEvent("PAYMENT_SUCCESS", request.getUserId());
      } else {
        notificationService.sendPaymentFailedNotification(request.getUserId());
        auditService.logPaymentEvent("PAYMENT_FAILED", request.getUserId());
      }

      //***** /Payment-specific logic
      return new PaymentProcessingResult(transaction);

    } catch (Exception e) {
      Transaction failedTransaction = TransactionCreator.createDeclinedTransaction(request, TransactionStatus.FAILED);
      notificationService.sendPaymentErrorNotification(request.getUserId());
      auditService.logPaymentEvent("PAYMENT_ERROR", request.getUserId());
      return new PaymentProcessingResult(failedTransaction);
    }
  }

  protected GatewayRequest createGatewayRequest(PaymentRequest request) {
    return new GatewayRequest(
      request.getUserId(),
      request.getMerchantId(),
      request.getAmount(),
      request.getCurrency(),
      request.getPaymentMethod()
    );
  }
}
