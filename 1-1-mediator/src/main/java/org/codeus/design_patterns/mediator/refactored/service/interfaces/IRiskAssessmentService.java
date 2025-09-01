package org.codeus.design_patterns.mediator.refactored.service.interfaces;

import org.codeus.design_patterns.mediator.common.PaymentRequest;

public interface IRiskAssessmentService {
    int calculateRiskScore(PaymentRequest request);
}
