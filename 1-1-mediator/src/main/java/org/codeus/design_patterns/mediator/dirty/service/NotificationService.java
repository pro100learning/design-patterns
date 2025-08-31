package org.codeus.design_patterns.mediator.dirty.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    
    public void sendFraudAlert(String userId, String message) {
        System.out.println("NotificationService: Sending fraud alert to " + userId + ": " + message);
    }
    
    public void sendLimitExceededNotification(String userId) {
        System.out.println("NotificationService: Sending limit exceeded notification to " + userId);
    }
    
    public void sendLimitWarning(String userId, BigDecimal limit) {
        System.out.println("NotificationService: Sending limit warning to " + userId + 
                          " (limit: " + limit + ")");
    }
    
    public void sendPaymentSuccessNotification(String userId) {
        System.out.println("NotificationService: Sending payment success notification to " + userId);
    }
    
    public void sendPaymentDeclinedNotification(String userId) {
        System.out.println("NotificationService: Sending payment declined notification to " + userId);
    }
    
    public void sendPaymentFailedNotification(String userId) {
        System.out.println("NotificationService: Sending payment failed notification to " + userId);
    }
    
    public void sendPaymentErrorNotification(String userId) {
        System.out.println("NotificationService: Sending payment error notification to " + userId);
    }
}
