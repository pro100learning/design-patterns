package org.codeus.design_patterns.mediator.refactored.service;

import org.codeus.design_patterns.mediator.refactored.service.interfaces.INotificationService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class NotificationService implements INotificationService {

  @Override
  public void sendFraudAlert(String userId, String message, List<String> fraudIndicators) {
    System.out.println("NotificationService: Sending fraud alert to " + userId +
      ": " + message + " | Indicators: " + String.join(", ", fraudIndicators));
  }

  @Override
  public void sendLimitExceededNotification(String userId, BigDecimal limit, BigDecimal requested) {
    System.out.println("NotificationService: Limit exceeded for " + userId +
      " | Limit: " + limit + " | Requested: " + requested);
  }

  @Override
  public void sendLimitWarning(String userId, BigDecimal limit) {
    System.out.println("NotificationService: Limit warning for " + userId + " (limit: " + limit + ")");
  }

  @Override
  public void sendPaymentSuccessNotification(String userId, String transactionId) {
    System.out.println("NotificationService: Payment success for " + userId +
      " | Transaction: " + transactionId);
  }

  @Override
  public void sendPaymentDeclinedNotification(String userId, List<String> reasons) {
    System.out.println("NotificationService: Payment declined for " + userId +
      " | Reasons: " + String.join(", ", reasons));
  }

  @Override
  public void sendPaymentFailedNotification(String userId, String reason) {
    System.out.println("NotificationService: Payment failed for " + userId + " | Reason: " + reason);
  }

  @Override
  public void sendPaymentErrorNotification(String userId, String error) {
    System.out.println("NotificationService: Payment error for " + userId + " | Error: " + error);
  }
}