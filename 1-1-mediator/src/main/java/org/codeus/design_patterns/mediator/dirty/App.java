package org.codeus.design_patterns.mediator.dirty;

import org.codeus.design_patterns.mediator.common.Transaction;
import org.codeus.design_patterns.mediator.dirty.payment.PaymentProcessingService;
import org.codeus.design_patterns.mediator.common.PaymentRequest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.math.BigDecimal;

@SpringBootApplication
public class App {

  public static void main(String[] args) {
    ApplicationContext context = SpringApplication.run(App.class, args);

    // Demo the tightly coupled system
    PaymentProcessingService paymentService = context.getBean(PaymentProcessingService.class);

    PaymentRequest request = new PaymentRequest("user123", "merchant456",
      new BigDecimal("1500"), "USD", "CREDIT_CARD");

    System.out.println("=== Processing Payment ===");
    Transaction result = paymentService.processPayment(request).getTransaction();
    System.out.println("Transaction ID: " + result.getTransactionId() +
      " Status: " + result.getStatus());

    System.out.println("\n=== Processing High-Risk Payment ===");
    PaymentRequest highRiskRequest = new PaymentRequest("new_user", "merchant456",
      new BigDecimal("15000"), "USD", "INTERNATIONAL");

    Transaction result2 = paymentService.processPayment(highRiskRequest).getTransaction();
    System.out.println("Transaction ID: " + result2.getTransactionId() +
      " Status: " + result2.getStatus());
  }
}