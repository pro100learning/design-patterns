package org.codeus.design_patterns.mediator.refactored;

import org.codeus.design_patterns.mediator.common.PaymentRequest;
import org.codeus.design_patterns.mediator.common.interfaces.IPaymentProcessingService;
import org.codeus.design_patterns.mediator.refactored.payment.interfaces.IPaymentMediator;
import org.codeus.design_patterns.mediator.refactored.payment.RichPaymentProcessingResult;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.math.BigDecimal;

@SpringBootApplication
public class App {

  public static void main(String[] args) {
    ApplicationContext context = SpringApplication.run(App.class, args);

    // Demo the loosely coupled system with mediator
    IPaymentProcessingService paymentMediator = context.getBean(IPaymentProcessingService.class);

    System.out.println("=== Processing Normal Payment ===");
    PaymentRequest normalRequest = new PaymentRequest("user123", "merchant456",
      new BigDecimal("1500"), "USD", "CREDIT_CARD");

    RichPaymentProcessingResult result1 = (RichPaymentProcessingResult)paymentMediator.processPayment(normalRequest);
    System.out.println("Result: " + (result1.isSuccessful() ? "SUCCESS" : "FAILED") +
      " | Transaction: " + result1.getTransaction().getTransactionId());

    System.out.println("\n=== Processing High-Risk Payment ===");
    PaymentRequest highRiskRequest = new PaymentRequest("new_user", "merchant456",
      new BigDecimal("15000"), "USD", "INTERNATIONAL");

    RichPaymentProcessingResult result2 = (RichPaymentProcessingResult)paymentMediator.processPayment(highRiskRequest);
    System.out.println("Result: " + (result2.isSuccessful() ? "SUCCESS" : "FAILED") +
      " | Transaction: " + result2.getTransaction().getTransactionId());

    if (result2.getValidationResult() != null && !result2.getValidationResult().errors().isEmpty()) {
      System.out.println("Validation Errors: " + String.join(", ", result2.getValidationResult().errors()));
    }
  }
}
