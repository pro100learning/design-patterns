package org.codeus.design_patterns.mediator.refactored.payment.interfaces;

import org.codeus.design_patterns.mediator.common.PaymentRequest;
import org.codeus.design_patterns.mediator.refactored.payment.RichPaymentProcessingResult;

public interface IPaymentMediator {
    RichPaymentProcessingResult processPayment(PaymentRequest request);
}

