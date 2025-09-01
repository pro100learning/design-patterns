package org.codeus.design_patterns.mediator.refactored.payment;

import org.codeus.design_patterns.mediator.common.GatewayRequest;
import org.codeus.design_patterns.mediator.common.Transaction;
import org.codeus.design_patterns.mediator.common.TransactionStatus;
import org.codeus.design_patterns.mediator.common.util.TransactionCreator;
import org.codeus.design_patterns.mediator.refactored.payment.interfaces.IPaymentGatewayService;
import org.springframework.stereotype.Service;

@Service
public class PaymentGatewayService implements IPaymentGatewayService {

  @Override
  public Transaction processPayment(GatewayRequest request) {
    System.out.println("PaymentGatewayService: Processing with external gateway...");

    // Simulate external payment processing
    TransactionStatus status = !request.getUserId().equals("blocked_user") ? TransactionStatus.APPROVED : TransactionStatus.FAILED;
    return TransactionCreator.createTransaction(request, status);
  }
}


