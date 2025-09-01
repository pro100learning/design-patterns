package org.codeus.design_patterns.mediator.refactored.payment.interfaces;

import org.codeus.design_patterns.mediator.common.GatewayRequest;
import org.codeus.design_patterns.mediator.common.Transaction;

public interface IPaymentGatewayService {
    Transaction processPayment(GatewayRequest request);
}
