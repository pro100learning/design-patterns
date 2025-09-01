package org.codeus.design_patterns.mediator.refactored.payment;

import org.codeus.design_patterns.mediator.common.PaymentRequest;
import org.codeus.design_patterns.mediator.common.Transaction;
import org.codeus.design_patterns.mediator.common.TransactionStatus;
import org.codeus.design_patterns.mediator.common.interfaces.IPaymentProcessingService;
import org.codeus.design_patterns.mediator.refactored.dto.ValidationResult;
import org.codeus.design_patterns.mediator.refactored.payment.interfaces.IPaymentService;
import org.codeus.design_patterns.mediator.refactored.payment.interfaces.IPaymentValidationMediator;
import org.codeus.design_patterns.mediator.refactored.service.interfaces.IAuditService;
import org.codeus.design_patterns.mediator.refactored.service.interfaces.INotificationService;
import org.codeus.design_patterns.mediator.refactored.service.interfaces.ITransactionService;
import org.springframework.stereotype.Component;

@Component
public class PaymentMediator implements IPaymentProcessingService {

  private final IPaymentService paymentService;
  private final IPaymentValidationMediator paymentValidationMediator;
  private final ITransactionService transactionService;
  private final INotificationService notificationService;
  private final IAuditService auditService;

  public PaymentMediator(IPaymentService paymentService,
                         IPaymentValidationMediator paymentValidationMediator,
                         ITransactionService transactionService,
                         INotificationService notificationService,
                         IAuditService auditService) {
    this.paymentService = paymentService;
    this.paymentValidationMediator = paymentValidationMediator;
    this.transactionService = transactionService;
    this.notificationService = notificationService;
    this.auditService = auditService;
  }

  @Override
  public RichPaymentProcessingResult processPayment(PaymentRequest request) {
    System.out.println("PaymentMediator: Orchestrating payment processing...");

    try {
      // Step 1: Validate payment
      ValidationResult validationResult = paymentValidationMediator.validate(request);

      // Step 1.1: Handle Cross-cutting concerns
      if (validationResult.isPaymentInvalid()) {
        return handleValidationFailure(request, validationResult);
      }

      // Step 2: Process payment through PaymentService
      Transaction transaction = paymentService.processPayment(request);

      // Step 2.1: Handle Cross-cutting concerns
      if (transaction.isSuccessful()) {
        return handleSuccessfulPayment(transaction, validationResult);
      } else {
        return handleGatewayFailure(transaction, validationResult);
      }

    } catch (Exception e) {
      return handleProcessingError(request, e);
    }
  }

  private RichPaymentProcessingResult handleValidationFailure(PaymentRequest request,
                                                              ValidationResult validationResult) {
    Transaction transaction = transactionService.createTransaction(request, TransactionStatus.DECLINED);

    auditService.logPaymentEvent("PAYMENT_DECLINED", request.getUserId(), transaction.getTransactionId());
    notificationService.sendPaymentDeclinedNotification(request.getUserId(), validationResult.errors());

    return new RichPaymentProcessingResult(transaction, validationResult, "Validation failed");
  }

  private RichPaymentProcessingResult handleSuccessfulPayment(Transaction transaction,
                                                              ValidationResult validationResult) {

    auditService.logPaymentEvent("PAYMENT_SUCCESS", transaction.getUserId(), transaction.getTransactionId());
    notificationService.sendPaymentSuccessNotification(transaction.getUserId(), transaction.getTransactionId());

    return new RichPaymentProcessingResult(transaction, validationResult, null);
  }

  private RichPaymentProcessingResult handleGatewayFailure(Transaction transaction,
                                                           ValidationResult validationResult) {
    auditService.logPaymentEvent("PAYMENT_FAILED", transaction.getUserId(), transaction.getTransactionId());
    notificationService.sendPaymentFailedNotification(transaction.getUserId(), "Gateway processing failed");

    return new RichPaymentProcessingResult(transaction, validationResult, "Gateway processing failed");
  }

  private RichPaymentProcessingResult handleProcessingError(PaymentRequest request, Exception e) {
    Transaction transaction = transactionService.createTransaction(request, TransactionStatus.FAILED);

    auditService.logPaymentEvent("PAYMENT_ERROR", request.getUserId(), transaction.getTransactionId());
    notificationService.sendPaymentErrorNotification(request.getUserId(), e.getMessage());

    return new RichPaymentProcessingResult(transaction, null, e.getMessage());
  }
}
