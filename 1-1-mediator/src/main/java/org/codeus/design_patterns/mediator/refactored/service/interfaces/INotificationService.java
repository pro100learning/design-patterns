package org.codeus.design_patterns.mediator.refactored.service.interfaces;

import java.math.BigDecimal;
import java.util.List;

public interface INotificationService {
  void sendFraudAlert(String userId, String message, List<String> fraudIndicators);

  void sendLimitExceededNotification(String userId, BigDecimal limit, BigDecimal requested);

  void sendLimitWarning(String userId, BigDecimal limit);

  void sendPaymentSuccessNotification(String userId, String transactionId);

  void sendPaymentDeclinedNotification(String userId, List<String> reasons);

  void sendPaymentFailedNotification(String userId, String reason);

  void sendPaymentErrorNotification(String userId, String error);
}
