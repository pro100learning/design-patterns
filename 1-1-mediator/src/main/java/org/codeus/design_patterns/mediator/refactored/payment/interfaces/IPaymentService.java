package org.codeus.design_patterns.mediator.refactored.payment.interfaces;

import org.codeus.design_patterns.mediator.common.PaymentRequest;
import org.codeus.design_patterns.mediator.common.Transaction;

public interface IPaymentService {

  Transaction processPayment(PaymentRequest request);
}
