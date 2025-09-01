package org.codeus.design_patterns.mediator.refactored.service;

import org.codeus.design_patterns.mediator.common.PaymentRequest;
import org.codeus.design_patterns.mediator.refactored.dto.DetailsCheckResult;
import org.codeus.design_patterns.mediator.refactored.service.interfaces.IPaymentDetailsValidationService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentDetailsValidationService implements IPaymentDetailsValidationService {

  @Override
  public DetailsCheckResult validate(PaymentRequest request) {
    List<String> errors = new ArrayList<>();

    if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) errors.add("Invalid amount");

    return new DetailsCheckResult(errors);
  }
}
