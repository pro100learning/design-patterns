package org.codeus.design_patterns.mediator.refactored.payment;

import org.codeus.design_patterns.mediator.common.PaymentProcessingResult;
import org.codeus.design_patterns.mediator.common.Transaction;
import org.codeus.design_patterns.mediator.refactored.dto.ValidationResult;

public class RichPaymentProcessingResult extends PaymentProcessingResult {
  private final ValidationResult validationResult;
  private final String errorMessage;

  public RichPaymentProcessingResult(Transaction transaction, ValidationResult validationResult, String errorMessage) {
    super(transaction);
    this.validationResult = validationResult;
    this.errorMessage = errorMessage;
  }

  // Getters
  public ValidationResult getValidationResult() { return validationResult; }
  public String getErrorMessage() { return errorMessage; }
}
