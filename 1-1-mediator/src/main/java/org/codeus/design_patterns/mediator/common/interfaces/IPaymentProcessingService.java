package org.codeus.design_patterns.mediator.common.interfaces;

import org.codeus.design_patterns.mediator.common.PaymentProcessingResult;
import org.codeus.design_patterns.mediator.common.PaymentRequest;

public interface IPaymentProcessingService {

  PaymentProcessingResult processPayment(PaymentRequest request);
}
