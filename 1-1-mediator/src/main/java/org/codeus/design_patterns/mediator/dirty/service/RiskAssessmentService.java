package org.codeus.design_patterns.mediator.dirty.service;

import org.codeus.design_patterns.mediator.common.PaymentRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class RiskAssessmentService {
    
    private final AuditService auditService;
    private final AccountService accountService;
    
    public RiskAssessmentService(AuditService auditService, 
                               AccountService accountService) {
        this.auditService = auditService;
        this.accountService = accountService;
    }
    
    public int calculateRiskScore(PaymentRequest request) {
        System.out.println("RiskAssessmentService: Calculating risk score...");
        
        int riskScore = 0;
        
        // Risk factors
        if (request.getAmount().compareTo(new BigDecimal("5000")) > 0) {
            riskScore += 30;
        }
        
        if (accountService.isNewAccount(request.getUserId())) {
            riskScore += 40;
        }
        
        if ("INTERNATIONAL".equals(request.getPaymentMethod())) {
            riskScore += 25;
        }
        
        // Log risk assessment
        auditService.logRiskAssessment(request.getUserId(), riskScore);
        
        return riskScore;
    }
}
