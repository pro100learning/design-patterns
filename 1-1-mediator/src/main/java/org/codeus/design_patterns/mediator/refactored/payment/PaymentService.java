package org.codeus.design_patterns.mediator.refactored.payment;

import org.codeus.design_patterns.mediator.common.GatewayRequest;
import org.codeus.design_patterns.mediator.common.PaymentRequest;
import org.codeus.design_patterns.mediator.common.Transaction;
import org.codeus.design_patterns.mediator.refactored.payment.interfaces.IPaymentGatewayService;
import org.codeus.design_patterns.mediator.refactored.payment.interfaces.IPaymentService;
import org.springframework.stereotype.Service;

@Service
public class PaymentService implements IPaymentService {

  private final IPaymentGatewayService paymentGatewayService;

  public PaymentService(IPaymentGatewayService paymentGatewayService) {
    this.paymentGatewayService = paymentGatewayService;
  }

  @Override
  public Transaction processPayment(PaymentRequest request) {
    return paymentGatewayService.processPayment(createGatewayRequest(request));
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
