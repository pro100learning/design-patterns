package org.codeus.design_patterns.mediator.dirty.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AuditService {
    
    public void logSecurityEvent(String eventType, String userId, BigDecimal amount) {
        System.out.println("AuditService: Logging security event - " + eventType + 
                          " for user " + userId + " amount " + amount);
    }
    
    public void logLimitViolation(String userId, BigDecimal amount, BigDecimal limit) {
        System.out.println("AuditService: Logging limit violation for user " + userId + 
                          " amount " + amount + " limit " + limit);
    }
    
    public void logRiskAssessment(String userId, int riskScore) {
        System.out.println("AuditService: Logging risk assessment for user " + userId + 
                          " score " + riskScore);
    }
    
    public void logPaymentEvent(String eventType, String userId) {
        System.out.println("AuditService: Logging payment event - " + eventType + 
                          " for user " + userId);
    }
}