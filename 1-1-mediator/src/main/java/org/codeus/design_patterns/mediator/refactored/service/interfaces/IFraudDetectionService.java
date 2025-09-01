package org.codeus.design_patterns.mediator.refactored.service.interfaces;

import org.codeus.design_patterns.mediator.common.PaymentRequest;
import org.codeus.design_patterns.mediator.refactored.dto.FraudCheckResult;

public interface IFraudDetectionService {
    FraudCheckResult checkForFraud(PaymentRequest request);
}
