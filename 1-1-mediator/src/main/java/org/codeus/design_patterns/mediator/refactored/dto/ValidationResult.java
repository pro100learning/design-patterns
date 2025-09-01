package org.codeus.design_patterns.mediator.refactored.dto;

import java.util.ArrayList;
import java.util.List;

public record ValidationResult(List<String> errors, FraudCheckResult fraudCheckResult,
                               LimitCheckResult limitCheckResult) {
    public ValidationResult(List<String> errors,
                            FraudCheckResult fraudCheckResult, LimitCheckResult limitCheckResult) {
        this.errors = new ArrayList<>(errors);
        this.fraudCheckResult = fraudCheckResult;
        this.limitCheckResult = limitCheckResult;
    }

    @Override
    public List<String> errors() {
        return new ArrayList<>(errors);
    }

    public void appendError(String error) {
        errors.add(error);//small hack
    }

    public ValidationResult addFraudCheckResult(FraudCheckResult fraudCheckResult) {
        return new ValidationResult(errors, fraudCheckResult, limitCheckResult);
    }


    public ValidationResult addLimitCheckResult(LimitCheckResult limitCheckResult) {
        return new ValidationResult(errors, fraudCheckResult, limitCheckResult);
    }

    public boolean isPaymentInvalid() {
      return !errors.isEmpty();
    }

}
