package org.codeus.design_patterns.mediator.refactored.payment.interfaces;

import org.codeus.design_patterns.mediator.common.PaymentRequest;
import org.codeus.design_patterns.mediator.refactored.dto.ValidationResult;

public interface IPaymentValidationMediator {

  ValidationResult validate(PaymentRequest request);

}
